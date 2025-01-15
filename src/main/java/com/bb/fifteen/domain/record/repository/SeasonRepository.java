package com.bb.fifteen.domain.record.repository;

import com.bb.fifteen.domain.record.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {
}
