package com.bb.fifteen.domain.record.service;

import com.bb.fifteen.domain.record.repository.PlayerRecordRepository;
import com.bb.fifteen.domain.record.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordUpdateService {

    private final PlayerRepository playerRepository;
    private final PlayerRecordRepository playerRecordRepository;


}
