package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.domain.record.entity.Season;
import com.bb.fifteen.domain.record.entity.SeasonTeam;
import com.bb.fifteen.domain.record.entity.Stage;
import com.bb.fifteen.domain.record.entity.Team;
import com.bb.fifteen.domain.record.repository.SeasonRepository;
import com.bb.fifteen.domain.record.repository.SeasonTeamRepository;
import com.bb.fifteen.domain.record.repository.StageRepository;
import com.bb.fifteen.domain.record.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
class RecordUpdateServiceTest {

    @Autowired
    RecordUpdateService recordUpdateService;

    @Autowired
    SeasonRepository seasonRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    SeasonTeamRepository seasonTeamRepository;

    @Test
    @DisplayName("시즌 데이터 업데이트 1 - LCK")
    void updateSeasonData() throws Exception {
        // given
        recordUpdateService.updateSeasonFromLCK();

        // when
        List<Stage> stages = stageRepository.findAll();
        List<Season> seasons = seasonRepository.findAll();

        // then
        Assertions.assertThat(stages.size()).isGreaterThan(0);
        Assertions.assertThat(seasons.size()).isGreaterThan(0);

    }

    @Test
    @DisplayName("팀 데이터 업데이트 1 - LCK")
    void updateTeamData() throws Exception {
        // given
        // season 먼저 update
        recordUpdateService.updateSeasonFromLCK();

        // when
        recordUpdateService.updateTeamFromLCK();

        List<Team> teams = teamRepository.findAll();
        List<SeasonTeam> seasonTeams = seasonTeamRepository.findAll();

        // then
        Assertions.assertThat(teams.size()).isGreaterThan(0);
        Assertions.assertThat(seasonTeams.size()).isGreaterThan(0);
    }

}