package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.domain.record.code.SourceDomainCode;
import com.bb.fifteen.domain.record.code.StageCode;
import com.bb.fifteen.domain.record.dto.crawling.RoundData;
import com.bb.fifteen.domain.record.dto.crawling.SeasonData;
import com.bb.fifteen.domain.record.dto.crawling.TeamProfileMetaData;
import com.bb.fifteen.domain.record.entity.Season;
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
                .stream()
                .map(season -> SeasonData
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
                        .build()
                ).forEach(seasonData -> {
                    crawlingService.crawlingTeamProfileMetaDataPerSeason(seasonData)
                            .stream()
                            .map(teamProfileMetaData -> {
                                String since = teamProfileMetaData.getSince();
                                String[] sinceSplit = since.split("-");

                                int foundedYear = Integer.parseInt(sinceSplit[0]);
                                int disbandedYear = sinceSplit.length == 2 ? Integer.parseInt(sinceSplit[1]) : -1;

                                Long sourceId = teamProfileMetaData.getTeamId();
                                String seasonInitialName = teamProfileMetaData.getInitialName();
                                String seasonName = teamProfileMetaData.getEngName();

                                Team team = teamRepository.findBySourceIdAndSourceDomain(sourceId, sourceDomainCode)
                                        .orElse(new Team(foundedYear, disbandedYear, sourceId, sourceDomainCode));

                                if (team.getId() == null) {
                                    teamRepository.save(team);
                                }

                                /**
                                 * TODO 여기까지
                                 */
                                return null;
                            });
                });

    }
}
