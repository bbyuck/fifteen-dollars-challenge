package com.bb.fifteen.domain.record.code;

import com.bb.fifteen.common.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 시즌 코드
 */
@Getter
@RequiredArgsConstructor
public enum SeasonCode implements Code {
    SPR("SPRING"),
    SUM("SUMMER"),
    WIN("WINTER"),
    CUP("LCK_CUP");

    private final String description;

    public static SeasonCode get(String crawledData) {
        return switch (crawledData) {
            case "SPRING" -> SPR;
            case "SUMMER" -> SUM;
            case "WINTER" -> WIN;
            case "LCK CUP" -> CUP;
            default -> null;
        };
    }
}
