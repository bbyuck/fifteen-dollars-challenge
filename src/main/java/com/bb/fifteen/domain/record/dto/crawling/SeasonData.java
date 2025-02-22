package com.bb.fifteen.domain.record.dto.crawling;

import com.bb.fifteen.domain.record.code.SeasonCode;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
public class SeasonData {
    private long tournamentId;
    private int year;
    private SeasonCode seasonCode;
    private List<RoundData> roundData;

    public String toString() {
        return year + " " + seasonCode.name();
    }
}
