package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.domain.record.code.StageCode;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.bb.fifteen.domain.record.code.StageCode.*;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

@Service
public class RecordUpdateService {

    private final Map<String, StageCode> STAGE_CODE_MAP = ofEntries(
            entry("RO16", ST00),
            entry("QUARTER FINALS", ST01),
            entry("SEMI FINALS", ST02),
            entry("3RD PLACE MATCH", ST03),
            entry("FINALS", ST04),
            entry("시드결정전", ST05),
            entry("RO12", ST06),
            entry("시드챌린지", ST05),
            entry("REGULAR SEASON", ST07),
            entry("PLAYOFFS", ST08),
            entry("Regular season", ST07),
            entry("Play-ins", ST09),
            entry("Playoffs", ST08)
    );


}
