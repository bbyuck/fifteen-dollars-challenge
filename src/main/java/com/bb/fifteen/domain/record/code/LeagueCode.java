package com.bb.fifteen.domain.record.code;

import com.bb.fifteen.common.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 리그 코드
 */
@Getter
@RequiredArgsConstructor
public enum LeagueCode implements Code {
    LCK("LCK", 1),
    MSI("MSI",2),
    WOR("WORLDS",5),
    EWC("EWC", 1);


    private final String description;
    private final int weight;
}
