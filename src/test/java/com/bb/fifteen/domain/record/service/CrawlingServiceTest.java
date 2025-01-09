package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.domain.record.dto.crawling.SeasonData;
import org.assertj.core.api.Assertions;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrawlingServiceTest {

    CrawlingService crawlingService = new CrawlingService();


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
    @DisplayName("POST 메소드 크롤링 1 - 시즌별 라운드 목록 크롤링")
    public void crawlingRoundListPerSeason() throws Exception {
        // given

        // when

        // then
    }
}