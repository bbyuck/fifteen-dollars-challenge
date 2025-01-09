package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.domain.record.code.SeasonCode;
import com.bb.fifteen.domain.record.dto.crawling.SeasonData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CrawlingService {

    public List<SeasonData> crawlingSeasonData() {
        String url = "https://www.lck.co.kr/stats/team";

        return getMethodCrawling(url).select("ul#seasonList > li")
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
                        .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

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
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .header("Content-Type", "application/x-www-form-urlencoded")
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
