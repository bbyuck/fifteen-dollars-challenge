package com.bb.fifteen.domain.record.repository;

import com.bb.fifteen.domain.record.code.SourceDomainCode;
import com.bb.fifteen.domain.record.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findBySourceIdAndSourceDomain(Long sourceId, SourceDomainCode sourceDomainCode);
}
