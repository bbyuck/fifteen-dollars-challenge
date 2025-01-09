package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.domain.record.code.StageCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.bb.fifteen.domain.record.code.StageCode.*;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordUpdateService {

    private final Map<String, StageCode> STAGE_CODE_MAP = ofEntries(
            entry("RO16", T16),
            entry("QUARTER FINALS", T08),
            entry("SEMI FINALS", T04),
            entry("3RD PLACE MATCH", T03),
            entry("FINALS", F00),
            entry("시드결정전", S00),
            entry("RO12", T12),
            entry("시드챌린지", S00),
            entry("REGULAR SEASON", R00),
            entry("PLAYOFFS", P00),
            entry("Regular season", R00),
            entry("Play-ins", P01),
            entry("Playoffs", P00)
    );


}
