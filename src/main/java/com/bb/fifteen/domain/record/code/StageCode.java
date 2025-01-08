package com.bb.fifteen.domain.record.code;

import com.bb.fifteen.common.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 시즌별 라운드 코드
 */
@Getter
@RequiredArgsConstructor
public enum StageCode implements Code {
    T16("16강", 1),
    T08("8강", 2),
    T04("4강", 3),
    T03("3,4위", 3),
    F00("결승전", 5),
    S00("시드결정전", 1),
    S12("12강", 1),
    R00("정규 시즌", 2),
    P00("플레이오프", 3),
    P01("Play-ins", 2);

    private final String description;
    private final int weight;
}
