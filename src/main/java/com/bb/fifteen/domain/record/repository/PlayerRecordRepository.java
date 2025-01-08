package com.bb.fifteen.domain.record.repository;

import com.bb.fifteen.domain.record.entity.PlayerRecord;
import com.bb.fifteen.domain.record.repository.custom.CustomPlayerRecordRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRecordRepository extends JpaRepository<PlayerRecord, Long>, CustomPlayerRecordRepository {
}
