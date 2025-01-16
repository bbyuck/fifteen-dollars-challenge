package com.bb.fifteen.domain.record.repository;

import com.bb.fifteen.domain.record.entity.SeasonTeamPlayerMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonTeamPlayerMappingRepository extends JpaRepository<SeasonTeamPlayerMapping, Long> {
}
