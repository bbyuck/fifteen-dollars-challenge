package com.bb.fifteen.domain.record.dto.crawling;

import com.bb.fifteen.domain.record.code.SeasonCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeasonData {
    private long id;
    private int year;
    private SeasonCode seasonCode;
}
