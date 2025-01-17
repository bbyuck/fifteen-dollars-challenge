package com.bb.fifteen.domain.record.repository;

import com.bb.fifteen.domain.record.entity.Player;
import com.bb.fifteen.domain.record.entity.Season;
import com.bb.fifteen.domain.record.entity.SeasonPlayer;
import com.bb.fifteen.domain.record.entity.SeasonTeamPlayerMapping;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeasonPlayerRepository extends JpaRepository<SeasonPlayer, Long> {

    @Query("select sp from SeasonPlayer sp where sp.season = :season and sp.player = :player")
    Optional<SeasonPlayer> findBySeasonAndPlayer(@Param("season") Season season, @Param("player") Player player);

    @Query("select sp from SeasonPlayer sp join sp.seasonTeamPlayerMappings stpm where stpm.seasonPlayer = sp and ")
    Optional<SeasonPlayer> findBySeasonTeamPlayerMappings();

    @EntityGraph(attributePaths = {"season"})
    List<SeasonPlayer> findSeasonPlayersBySeason(Season season);

}
