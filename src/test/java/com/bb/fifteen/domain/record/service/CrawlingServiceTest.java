package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.common.util.ResourceLoader;
import com.bb.fifteen.domain.record.dto.crawling.SeasonData;
import com.bb.fifteen.domain.record.dto.crawling.TeamProfileMetaData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class CrawlingServiceTest {

    CrawlingService crawlingService = new CrawlingService(new ResourceLoader());


    @Test
    @DisplayName("GET 메소드 크롤링 1 - 시즌 목록 크롤링")
    public void crawlingSeasonList() throws Exception {
        // given
        List<SeasonData> seasonDataList = crawlingService.crawlingSeasonData();

        // then
        Assertions.assertThat(seasonDataList).isNotEmpty();
        Assertions.assertThat(seasonDataList.size()).isEqualTo(29);
    }

    @Test
    @DisplayName("GET 메소드 크롤링 2 - 시즌별 팀 프로필 목록 크롤링")
    public void crawlingTeamProfileMetadataList() throws Exception {
        // given

        // season data 크롤링
        List<SeasonData> seasonData = crawlingService.crawlingSeasonData();

        //
        List<TeamProfileMetaData> teamProfileMetaDataList = crawlingService.crawlingTeamProfileMetaDataPerSeason(seasonData.get(0));

        // when

        // then
    }
}