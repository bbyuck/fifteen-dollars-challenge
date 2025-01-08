package com.bb.fifteen.domain.record.repository;

import com.bb.fifteen.domain.record.entity.Coach;
import com.bb.fifteen.domain.record.repository.custom.CustomCoachRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoachRepository extends JpaRepository<Coach, Long>, CustomCoachRepository {
}
