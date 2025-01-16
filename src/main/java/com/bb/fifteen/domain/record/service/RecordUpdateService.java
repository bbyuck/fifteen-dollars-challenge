package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.domain.record.code.SourceDomainCode;
import com.bb.fifteen.domain.record.code.StageCode;
import com.bb.fifteen.domain.record.dto.crawling.RoundData;
import com.bb.fifteen.domain.record.dto.crawling.SeasonData;
import com.bb.fifteen.domain.record.entity.Season;
import com.bb.fifteen.domain.record.entity.SeasonTeam;
import com.bb.fifteen.domain.record.entity.Stage;
import com.bb.fifteen.domain.record.entity.Team;
import com.bb.fifteen.domain.record.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordUpdateService {

    /**
     * Repository
     */
    private final PlayerRepository playerRepository;
    private final PlayerRecordRepository playerRecordRepository;
    private final SeasonRepository seasonRepository;
    private final StageRepository stageRepository;
    private final TeamRepository teamRepository;
    private final SeasonTeamRepository seasonTeamRepository;

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
                                        String since = teamProfileMetaData.getSince();
                                        int foundedYear = -1;
                                        int disbandedYear = -1;

                                        if (since != null) {
                                            String[] sinceSplit = since.split("-");
                                            foundedYear = Integer.parseInt(sinceSplit[0]);
                                            disbandedYear = sinceSplit.length == 2 ? Integer.parseInt(sinceSplit[1]) : -1;
                                        }

                                        Long sourceId = teamProfileMetaData.getTeamId();
                                        String seasonTeamInitialName = teamProfileMetaData.getInitialName();
                                        String seasonTeamName = teamProfileMetaData.getEngName();

                                        /**
                                         * sourceId - source domain code 로 팀 기준 정보 조회
                                         * 없으면 새 Entity 생성
                                         */
                                        Team team = teamRepository.findBySourceIdAndSourceDomain(sourceId, sourceDomainCode)
                                                .orElse(new Team(foundedYear, disbandedYear, sourceId, sourceDomainCode));

                                        if (team.getId() == null) {
                                            teamRepository.save(team);
                                        }

                                        /**
                                         * Season - Team Entity로 SeasonTeam 조회
                                         * 없으면 새 Entity 생성
                                         */
                                        SeasonTeam seasonTeam = seasonTeamRepository.findBySeasonAndTeam(season, team)
                                                .orElse(new SeasonTeam(season, team, seasonTeamName, seasonTeamInitialName));
                                        if (seasonTeam.getId() == null) {
                                            seasonTeamRepository.save(seasonTeam);
                                        }

                                    });
                        }
                );
    }

}
