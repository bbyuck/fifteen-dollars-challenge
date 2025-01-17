package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.domain.record.code.SourceDomainCode;
import com.bb.fifteen.domain.record.code.StageCode;
import com.bb.fifteen.domain.record.dto.crawling.PlayerProfileSeasonalMetaData;
import com.bb.fifteen.domain.record.dto.crawling.RoundData;
import com.bb.fifteen.domain.record.dto.crawling.SeasonData;
import com.bb.fifteen.domain.record.dto.crawling.TeamProfileMetaData;
import com.bb.fifteen.domain.record.entity.*;
import com.bb.fifteen.domain.record.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordUpdateService {

    /**
     * Repository
     */
    private final SeasonRepository seasonRepository;
    private final StageRepository stageRepository;

    private final PlayerRepository playerRepository;
    private final SeasonPlayerRepository seasonPlayerRepository;
    private final PlayerRecordRepository playerRecordRepository;

    private final TeamRepository teamRepository;
    private final SeasonTeamRepository seasonTeamRepository;
    private final TeamRecordRepository teamRecordRepository;

    private final SeasonTeamPlayerMappingRepository seasonTeamPlayerMappingRepository;

    /**
     * Service
     */
    private final CrawlingService crawlingService;


    @Transactional
    public void updateSeasonFromLCK() {
        SourceDomainCode sourceDomain = SourceDomainCode.LCK;

        seasonRepository.saveAll(
                crawlingService.crawlingSeasonDataFromLCK()
                        .stream()
                        .map(seasonData -> {
                            Season season = new Season(seasonData.getYear(), seasonData.getSeasonCode(), seasonData.getTournamentId(), sourceDomain);
                            List<Stage> seasonStages = seasonData.getRoundData()
                                    .stream()
                                    .map(roundData -> new Stage(
                                            season,
                                            StageCode.fromString(roundData.getLabel()),
                                            roundData.getStageId(),
                                            sourceDomain
                                    ))
                                    .collect(Collectors.toList());

                            stageRepository.saveAll(seasonStages);
                            season.addAll(seasonStages);
                            return season;
                        })
                        .collect(Collectors.toList())
        );
    }


    @Transactional
    public void updateTeamFromLCK() {
        SourceDomainCode sourceDomainCode = SourceDomainCode.LCK;

        seasonRepository.findAllWithStages()
                .forEach(season -> {
                            SeasonData seasonData = SeasonData
                                    .builder()
                                    .tournamentId(season.getSourceId())
                                    .roundData(season.getStages()
                                            .stream()
                                            .map(stage -> RoundData
                                                    .builder()
                                                    .stageId(stage.getSourceId())
                                                    .build())
                                            .collect(Collectors.toList())
                                    )
                                    .build();

                            crawlingService.crawlingTeamProfileMetaDataPerSeason(seasonData)
                                    .forEach(teamProfileMetaData -> {
                                        Long sourceId = teamProfileMetaData.getTeamId();
                                        String seasonTeamInitialName = teamProfileMetaData.getInitialName();
                                        String seasonTeamName = teamProfileMetaData.getEngName();

                                        /**
                                         * sourceId - source domain code 로 팀 기준 정보 조회
                                         * 없으면 새 Entity 생성
                                         */
                                        Team team = teamRepository.findBySourceIdAndSourceDomain(sourceId, sourceDomainCode)
                                                .orElseGet(() -> {
                                                    String since = teamProfileMetaData.getSince();
                                                    int foundedYear = -1;
                                                    int disbandedYear = -1;

                                                    if (since != null) {
                                                        String[] sinceSplit = since.split("-");
                                                        foundedYear = Integer.parseInt(sinceSplit[0]);
                                                        disbandedYear = sinceSplit.length == 2 ? Integer.parseInt(sinceSplit[1]) : -1;
                                                    }

                                                    return teamRepository.save(new Team(foundedYear, disbandedYear, sourceId, sourceDomainCode));
                                                });

                                        /**
                                         * Season - Team Entity로 SeasonTeam 조회
                                         * 없으면 새 Entity 생성
                                         */
                                        seasonTeamRepository.findBySeasonAndTeam(season, team)
                                                .orElseGet(() -> seasonTeamRepository.save(new SeasonTeam(season, team, seasonTeamName, seasonTeamInitialName)));
                                    });
                        }
                );
    }

    @Transactional
    public void updatePlayerFromLCK() {
        SourceDomainCode sourceDomainCode = SourceDomainCode.LCK;
        List<SeasonTeam> allSeasonTeam = seasonTeamRepository.findAllWithSeasonAndTeam();

        // 크롤링 해온 데이터
        Set<Map.Entry<Long, List<PlayerProfileSeasonalMetaData>>> allSeasonPlayerMetaDataPerPlayer = allSeasonTeam
                .stream()
                .map(seasonTeam ->
                        crawlingService.crawlingPlayerProfileMetaDataPerSeasonTeam(
                                TeamProfileMetaData
                                        .builder()
                                        .seasonData(
                                                SeasonData
                                                        .builder()
                                                        .tournamentId(seasonTeam.getSeason().getSourceId())
                                                        .build()
                                        )
                                        .teamId(seasonTeam.getTeam().getSourceId())
                                        .build()
                        )
                )
                .reduce(new HashMap<>(), (map1_, map2_) -> {
                    map2_.forEach((key, value) -> {
                        map1_.merge(
                                key,
                                new ArrayList<>(value),
                                (exsitingList, newList) -> {
                                    exsitingList.addAll(newList);
                                    return exsitingList;
                                });
                    });
                    return map1_;
                }).entrySet();

        List<SeasonPlayer> seasonPlayers = new ArrayList<>();

        /**
         * 크롤링 해온 데이터 순회
         *
         * playerId : [ seasonPlayerData, ...]
         */
        for (Map.Entry<Long, List<PlayerProfileSeasonalMetaData>> entry : allSeasonPlayerMetaDataPerPlayer) {
            Long playerId = entry.getKey();
            List<PlayerProfileSeasonalMetaData> seasonPlayerDataList = entry.getValue();

            /**
             * player 기준정보 Entity
             */
            Player player = playerRepository.findBySourceIdAndSourceDomain(playerId, sourceDomainCode)
                    .orElseGet(() -> {
                        String korName = "";
                        String engName = "";

                        for (PlayerProfileSeasonalMetaData seasonPlayerData : seasonPlayerDataList) {
                            if (!StringUtils.hasText(korName)) {
                                korName = seasonPlayerData.getKorName();
                            }
                            if (!StringUtils.hasText(engName)) {
                                engName = seasonPlayerData.getEngName();
                            }
                        }

                        return playerRepository.save(new Player(korName, engName, playerId, sourceDomainCode));
                    });

            /**
             * TODO SUCCESS - 25.01.17
             * 1. in 절로 바꿔서 season select
             * 2. Map으로 Season.id를 키로 해 메모리에 올려 관리
             */
            Map<Long, Season> seasons = seasonRepository.findBySourceIds(
                            seasonPlayerDataList
                                    .stream()
                                    .map(seasonPlayerData -> seasonPlayerData
                                            .getSeasonTeamProfileMetaData()
                                            .getSeasonData()
                                            .getTournamentId())
                                    .collect(Collectors.toList()
                                    ))
                    .stream()
                    .collect(Collectors.toMap(
                            season -> season.getId(),
                            season -> season
                    ));

            /**
             * TODO
             * 1. season, player외에 SeasonTeamPlayerMapping 엔티티로 SeasonPlayer 가져올 수 있도록 변경
             * 2. SeasonTeamPlayerMapping은 season, team을 파라미터로 해서 in절에 녹이기 (Native)
             *
             * season, player, team, seasonTeam으로 SeasonTeamPlayerMapping 조회
             *
             * 3. 없다면 Mapping Entity도 추가
             */

//
//            seasonPlayers.addAll(
//                    seasonPlayerDataList.stream().map(seasonPlayerData -> {
//                                Season season = seasonRepository.findBySourceIdAndSourceDomain(
//                                                seasonPlayerData.getSeasonData().getTournamentId(), sourceDomainCode)
//                                        .orElseThrow(() -> new RuntimeException("시즌 정보가 없습니다."));
//
//                                return seasonPlayerRepository.findBySeasonAndPlayer(season, player)
//                                        .orElseGet(() -> new SeasonPlayer(season, player, seasonPlayerData.getNickname(), seasonPlayerData.getPositionCode()));
//                            })
//                            .collect(Collectors.toList()));
        }

        seasonPlayerRepository.saveAll(seasonPlayers);
    }


}
