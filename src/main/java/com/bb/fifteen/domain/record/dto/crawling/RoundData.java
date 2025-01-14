package com.bb.fifteen.domain.record.dto.crawling;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoundData {
    private long id;
    private String label;

}
