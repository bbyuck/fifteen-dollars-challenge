package com.bb.fifteen.domain.record.repository;

import com.bb.fifteen.domain.record.code.SourceDomainCode;
import com.bb.fifteen.domain.record.entity.Player;
import com.bb.fifteen.domain.record.repository.custom.CustomPlayerRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long>, CustomPlayerRepository {

    @Query("select p from Player p where p.sourceId = :playerId and p.sourceDomain = :sourceDomainCode")
    Optional<Player> findBySourceIdAndSourceDomain(@Param("playerId") Long playerId, @Param("sourceDomainCode") SourceDomainCode sourceDomainCode);
}
