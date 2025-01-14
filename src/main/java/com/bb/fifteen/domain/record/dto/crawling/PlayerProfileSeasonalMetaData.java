package com.bb.fifteen.domain.record.dto.crawling;

import com.bb.fifteen.domain.record.code.PositionCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerProfileSeasonalMetaData {

    private long playerId;
    private String nickname;
    private String korName;
    private String engName;
    private PositionCode positionCode;
    private SeasonData seasonData;

}
