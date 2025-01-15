package com.bb.fifteen.domain.record.code;

import com.bb.fifteen.common.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SourceDomainCode implements Code {

    LCK("LCK 공식 홈", "https://www.lck.co.kr");

    private final String description;
    private final String url;
}
