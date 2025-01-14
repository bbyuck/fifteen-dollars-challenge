package com.bb.fifteen.domain.record.code;

import com.bb.fifteen.common.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;

/**
 * 시즌별 라운드 코드
 */
@Getter
@RequiredArgsConstructor
public enum StageCode implements Code {
    T16("16강", Set.of("RO16"), 1),
    T08("8강",  Set.of("QUARTER FINALS"), 2),
    T04("4강",  Set.of("SEMI FINALS"), 3),
    T03("3,4위", Set.of("3RD PLACE MATCH"), 3),
    F00("결승전", Set.of("FINALS"), 5),
    S00("시드결정전", Set.of("시드결정전", "시드챌린지"),1),
    T12("12강", Set.of("RO12"), 1),
    R00("정규 시즌", Set.of("REGULAR SEASON", "Regular season"), 2),
    P00("플레이오프", Set.of("PLAYOFFS", "Playoffs"),3),
    P01("Play-ins", Set.of("Play-ins"), 2);

    public static StageCode fromString(String value) {
        return Arrays.stream(StageCode.values()).filter(stageCode -> stageCode.getValueSet().contains(value)).findFirst().orElse(R00);
    }

    private final String description;
    private final Set<String> valueSet;
    private final int weight;
}
