package com.bb.fifteen.domain.record.repository;

import com.bb.fifteen.domain.record.entity.Season;
import com.bb.fifteen.domain.record.entity.SeasonTeam;
import com.bb.fifteen.domain.record.entity.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeasonTeamRepository extends JpaRepository<SeasonTeam, Long> {

    @Query("select st from SeasonTeam st where st.season = :season and st.team = :team")
    Optional<SeasonTeam> findBySeasonAndTeam(@Param("season") Season season, @Param("team") Team team);

    @EntityGraph(attributePaths = {"season", "team"})
    @Query("select st from SeasonTeam st")
    List<SeasonTeam> findAllWithSeasonAndTeam();
}
