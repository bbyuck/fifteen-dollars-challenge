package com.bb.fifteen.domain.record.dto.crawling;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamProfileMetaData {

    private Long teamId;            // id

    private String engName;         // 영어명

    private String initialName;     // 팀 명 이니셜

    private SeasonData seasonData;  // 대상 시즌

    private String since;           // 연혁

/**
 * 추후에 메소드로 제공
 *
 */
//    private int foundedYear;        // 설립년도
//    private int disbandmentYear;    // 해체연도
//    private String korName;         // 한글명

}
