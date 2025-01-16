package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.common.util.ResourceLoader;
import com.bb.fifteen.domain.record.code.PositionCode;
import com.bb.fifteen.domain.record.code.SeasonCode;
import com.bb.fifteen.domain.record.dto.crawling.PlayerProfileSeasonalMetaData;
import com.bb.fifteen.domain.record.dto.crawling.RoundData;
import com.bb.fifteen.domain.record.dto.crawling.SeasonData;
import com.bb.fifteen.domain.record.dto.crawling.TeamProfileMetaData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final ResourceLoader resourceLoader;
    private final String TARGET_SERVER_DOMAIN = "https://www.lck.co.kr";

    /**
     * 시즌 정보 크롤링 하위 메소드
     * 시즌 정보 중 라운드 (stage) 관련 정보 파싱
     *
     * @param crawledDocument
     * @return
     */
    private Map<Long, List<Map<String, String>>> parseSeasonRounds(Document crawledDocument) {
        Elements scripts = crawledDocument.select("script");
        String targetVariableDeclaration = "const rounds";
        String variableRegExp = "\\s*=\\s*(\\{.*?\\});";
        Element roundsContainScript = scripts.stream().filter(script -> script.html().contains(targetVariableDeclaration)).findFirst().orElseThrow(() -> new IllegalStateException("rounds 정보를 찾지 못했습니다."));
        String scriptContent = roundsContainScript.html();


        Pattern pattern = Pattern.compile(
                new StringBuilder()
                        .append(targetVariableDeclaration)
                        .append(variableRegExp).toString());
        Matcher matcher = pattern.matcher(scriptContent);

        if (matcher.find()) {
            String jsonString = matcher.group(1);

            // JSON 파싱
            ObjectMapper mapper = new ObjectMapper();

            try {
                return mapper.readValue(jsonString, new TypeReference<>() {
                });
            } catch (JsonMappingException e) {
                log.error(e.getMessage(), e);
                log.error("파싱한 변수를 JSON으로 매핑하는 과정에서 문제가 발생했습니다.");
                throw new IllegalStateException(e.getMessage());
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
                log.error("JSON 처리 과정에서 문제가 발생했습니다.");
                throw new IllegalStateException(e.getMessage());
            }
        }

        throw new IllegalStateException("rounds 정보를 찾지 못했습니다.");
    }

    /**
     * 시즌 정보 크롤링
     * 시즌 목록
     * 시즌별 스테이지 목록
     *
     * @return
     */
    public List<SeasonData> crawlingSeasonDataFromLCK() {
        log.debug("시즌 정보 크롤링 start");
        String targetUri = TARGET_SERVER_DOMAIN + "/stats/team";

        Document crawledDocument = getMethodCrawling(targetUri);
        Map<Long, List<Map<String, String>>> roundsMap = parseSeasonRounds(crawledDocument);

        return crawledDocument.select("ul#seasonList > li")
                .stream()
                .map(li -> {
                    String crawledSeasonLabel = li.selectFirst("span").text();
                    String dataIdAttr = li.attr("data-id");

                    if (!StringUtils.hasText(dataIdAttr)) {
                        return null;
                    }

                    long dataId = Long.parseLong(dataIdAttr);

                    return SeasonData
                            .builder()
                            .tournamentId(dataId)
                            .year(Integer.parseInt(crawledSeasonLabel.substring(0, 4)))
                            .seasonCode(SeasonCode.get(crawledSeasonLabel.substring(5)))
                            .roundData(roundsMap.get(dataId)
                                    .stream()
                                    .map(roundInfo -> RoundData
                                            .builder()
                                            .stageId(Long.valueOf(roundInfo.get("value")))
                                            .label(roundInfo.get("label"))
                                            .build()
                                    )
                                    .collect(Collectors.toList()))
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 시즌별 팀 프로필 메타 정보 크롤링
     *
     * @return
     */
    public List<TeamProfileMetaData> crawlingTeamProfileMetaDataPerSeason(SeasonData seasonData) {
        log.debug("시즌별 팀 프로필 메타 정보 크롤링 ====== {}", seasonData);

        String targetUri = TARGET_SERVER_DOMAIN + "/team/profile";
        Map<String, String> parameters = Map.of("tournamentId", String.valueOf(seasonData.getTournamentId()));

        Document seasonTeamsUl = postMethodCrawling(targetUri, parameters);
        Elements teamLis = seasonTeamsUl.select("li.item");

        return teamLis
                .stream()
                .map(element -> {
                            String[] idSplit = element.select("a.cont_top").attr("href").split("/");
                    String since = element.select("p.since").text();
                    return TeamProfileMetaData
                                    .builder()
                                    .teamId(Long.valueOf(idSplit[idSplit.length - 1]))
                                    .engName(element.select("p.team").text())
                                    .initialName(element.select("strong.team_initial").text())
                                    .since(StringUtils.hasText(since) ? since.substring(6) : null)
                                    .seasonData(seasonData)
                                    .build();
                        }
                )
                .collect(Collectors.toList());
    }

    /**
     * 시즌 팀 로스터 조회
     *
     * @param url
     * @return
     */
    public Map<Long, List<PlayerProfileSeasonalMetaData>> crawlingPlayerProfileMetaDataPerSeasonTeam(TeamProfileMetaData teamProfileMetaData) {
        log.debug("시즌 팀 로스터 크롤링 ====== {} {}", teamProfileMetaData.getSeasonData(), teamProfileMetaData.getInitialName());

        String targetUri = TARGET_SERVER_DOMAIN + "/teams/profile/roster";
        Map<String, String> parameters = Map.of(
                "teamId", String.valueOf(teamProfileMetaData.getTeamId()),
                "tournamentId", String.valueOf(teamProfileMetaData.getSeasonData().getTournamentId()));

        Map<Long, List<PlayerProfileSeasonalMetaData>> answer = new HashMap<>();

        Document document = postMethodCrawling(targetUri, parameters);
        Elements positionElements = document.select("div.position");

        for (Element positionElement : positionElements) {
            String position = positionElement.selectFirst("em.tit_pos").text();

            Elements positionPlayerATags = positionElement.select("a.link_player");

            for (Element positionPlayerATag : positionPlayerATags) {

                // playerId
                String[] href = positionPlayerATag.attr("href").split("/");
                long playerId = Long.parseLong(href[href.length - 1]);
                answer.putIfAbsent(playerId, new ArrayList<>());

                // name
                String name = positionPlayerATag.select("strong.name").text();

                // nickname
                String nickname = positionPlayerATag.select("p.nick").text();

                // engname
                String playerEngName = positionPlayerATag.select("p.txt_eng").text();

                List<PlayerProfileSeasonalMetaData> playerProfileSeasonalMetaDataList = answer.get(playerId);

                playerProfileSeasonalMetaDataList.add(
                        PlayerProfileSeasonalMetaData
                                .builder()
                                .playerId(playerId)
                                .engName(playerEngName)
                                .korName(name)
                                .nickname(nickname)
                                .positionCode(PositionCode.fromString(position))
                                .seasonData(teamProfileMetaData.getSeasonData())
                                .build());
            }

        }

        return answer;
    }

    private Document getMethodCrawling(String url) {
        try {
            // 타겟 URL HTML 가져오기
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            log.error("GET 메소드 크롤링에 실패했습니다.");
            log.error("target ====== {}", url);
            log.error(e.getMessage(), e);
            throw new IllegalStateException();
        }
    }

    private Document postMethodCrawling(String url, Map<String, String> parameters) {
        try {
            Connection.Response response = Jsoup.connect(url)
                    .method(Connection.Method.POST)
                    .data(parameters)
                    .header("User-Agent", "PostmanRuntime/7.43.0")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Connection", "keep-alive")
                    .timeout(10000)
                    .execute();

            return response.parse();
        } catch (IOException e) {
            log.error("POST 메소드 크롤링에 실패했습니다.");
            log.error("target ====== {}", url);
            log.error(e.getMessage(), e);
            throw new IllegalStateException();
        }
    }
}
