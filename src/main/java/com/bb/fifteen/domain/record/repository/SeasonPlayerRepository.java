package com.bb.fifteen.domain.record.repository;

import com.bb.fifteen.domain.record.entity.Player;
import com.bb.fifteen.domain.record.entity.Season;
import com.bb.fifteen.domain.record.entity.SeasonPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeasonPlayerRepository extends JpaRepository<SeasonPlayer, Long> {

    @Query("select sp from SeasonPlayer sp where sp.season = :season and sp.player = :player")
    Optional<SeasonPlayer> findBySeasonAndPlayer(@Param("season") Season season, @Param("player") Player player);
}
