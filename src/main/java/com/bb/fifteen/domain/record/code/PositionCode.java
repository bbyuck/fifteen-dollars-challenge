package com.bb.fifteen.domain.record.code;

import com.bb.fifteen.common.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PositionCode implements Code {

    TOP("탑"),
    JUG("정글"),
    MID("미드"),
    ADC("원딜"),
    SUP("서포터"),
    TBD("미정");

    private final String description;

    public static PositionCode fromString(String value) {
        return Arrays.stream(PositionCode.values()).filter(positionCode -> positionCode.name().equalsIgnoreCase(value)).findFirst().orElse(TBD);
    }
}
