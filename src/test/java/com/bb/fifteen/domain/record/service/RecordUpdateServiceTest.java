package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.domain.record.entity.Season;
import com.bb.fifteen.domain.record.entity.Stage;
import com.bb.fifteen.domain.record.repository.SeasonRepository;
import com.bb.fifteen.domain.record.repository.StageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RecordUpdateServiceTest {

    @Autowired
    RecordUpdateService recordUpdateService;

    @Autowired
    SeasonRepository seasonRepository;

    @Autowired
    StageRepository stageRepository;

    @Test
    @DisplayName("시즌 데이터 업데이트 1 - LCK")
    public void updateSeasonData() throws Exception {
        // given
        recordUpdateService.updateSeasonFromLCK();

        // when
        List<Stage> stages = stageRepository.findAll();
        List<Season> seasons = seasonRepository.findAll();

        // then
        Assertions.assertThat(stages.size()).isGreaterThan(0);
        Assertions.assertThat(seasons.size()).isGreaterThan(0);

    }

}