package com.bb.fifteen.domain.record.repository;

import com.bb.fifteen.domain.record.entity.TeamRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRecordRepository extends JpaRepository<TeamRecord, Long> {
}
