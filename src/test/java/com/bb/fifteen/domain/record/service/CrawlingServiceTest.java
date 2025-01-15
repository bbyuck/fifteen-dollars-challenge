package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.common.util.ResourceLoader;
import com.bb.fifteen.domain.record.dto.crawling.PlayerProfileSeasonalMetaData;
import com.bb.fifteen.domain.record.dto.crawling.SeasonData;
import com.bb.fifteen.domain.record.dto.crawling.TeamProfileMetaData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

class CrawlingServiceTest {

    CrawlingService crawlingService = new CrawlingService(new ResourceLoader());


    @Test
    @DisplayName("GET 메소드 크롤링 1 - 시즌 목록 크롤링")
    public void crawlingSeasonList() throws Exception {
        // given
        List<SeasonData> seasonDataList = crawlingService.crawlingSeasonDataFromLCK();

        // then
        Assertions.assertThat(seasonDataList).isNotEmpty();
        Assertions.assertThat(seasonDataList.size()).isEqualTo(29);
    }

    @Test
    @DisplayName("POST 메소드 크롤링 1 - 시즌별 팀 프로필 목록 크롤링")
    public void crawlingTeamProfileMetadataList() throws Exception {
        // given

        // season data 크롤링
        List<SeasonData> seasonData = crawlingService.crawlingSeasonDataFromLCK();

        //
        List<TeamProfileMetaData> teamProfileMetaDataList = crawlingService.crawlingTeamProfileMetaDataPerSeason(seasonData.get(0));

        // when

        System.out.println("teamProfileMetaDataList = " + teamProfileMetaDataList);

        // then
    }
    
    @Test
    @DisplayName("POST 메소드 크롤링 2 - 시즌별 로스터 조회")
    public void crawlingPlayerPerSeasonTeam() throws Exception {
        // given
        // 시즌 정보 목록
        List<SeasonData> seasonDataList = crawlingService.crawlingSeasonDataFromLCK();
//        Thread.sleep(1000);

        List<Map<Long, List<PlayerProfileSeasonalMetaData>>> allData = new ArrayList<>();

        for (SeasonData seasonData : seasonDataList) {
//            Thread.sleep(1000);
            List<TeamProfileMetaData> seasonTeamDataList = crawlingService.crawlingTeamProfileMetaDataPerSeason(seasonData);

            for (TeamProfileMetaData seasonTeamData : seasonTeamDataList) {
//                Thread.sleep(1000);
                allData.add(crawlingService.crawlingPlayerProfileMetaDataPerSeasonTeam(seasonTeamData));
            }
        }

        // when

        Map<Long, List<PlayerProfileSeasonalMetaData>> allPlayerData = allData.stream()
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        },
                        LinkedHashMap::new
                ));

        // then


    }
}