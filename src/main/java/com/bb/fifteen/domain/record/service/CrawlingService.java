package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.common.util.ResourceLoader;
import com.bb.fifteen.domain.record.code.SeasonCode;
import com.bb.fifteen.domain.record.dto.crawling.RoundData;
import com.bb.fifteen.domain.record.dto.crawling.SeasonData;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final ResourceLoader resourceLoader;

    public List<SeasonData> crawlingSeasonData() {
        String url = "https://www.lck.co.kr/stats/team";

        Document crawledDocument = getMethodCrawling(url);
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
                            .id(dataId)
                            .year(Integer.parseInt(crawledSeasonLabel.substring(0, 4)))
                            .seasonCode(SeasonCode.get(crawledSeasonLabel.substring(5)))
                            .roundData(roundsMap.get(dataId)
                                    .stream()
                                    .map(roundInfo -> RoundData
                                            .builder()
                                            .id(Long.valueOf(roundInfo.get("value")))
                                            .label(roundInfo.get("label"))
                                            .build()
                                    )
                                    .collect(Collectors.toList()))
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

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

//    public List<TeamMetaData> crawlingTeamMetaData(List<SeasonData> seasonDataList) {
//
//    }

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
                    .timeout(5000)
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
