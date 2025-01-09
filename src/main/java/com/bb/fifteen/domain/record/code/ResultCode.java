package com.bb.fifteen.domain.record.code;

import com.bb.fifteen.common.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultCode implements Code {
    L("패배"),
    W("승리");

    private final String description;
}
