package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.domain.record.code.SourceDomainCode;
import com.bb.fifteen.domain.record.code.StageCode;
import com.bb.fifteen.domain.record.entity.Season;
import com.bb.fifteen.domain.record.entity.Stage;
import com.bb.fifteen.domain.record.repository.PlayerRecordRepository;
import com.bb.fifteen.domain.record.repository.PlayerRepository;
import com.bb.fifteen.domain.record.repository.SeasonRepository;
import com.bb.fifteen.domain.record.repository.StageRepository;
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

    private final PlayerRepository playerRepository;
    private final PlayerRecordRepository playerRecordRepository;
    private final SeasonRepository seasonRepository;
    private final StageRepository stageRepository;
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

}
