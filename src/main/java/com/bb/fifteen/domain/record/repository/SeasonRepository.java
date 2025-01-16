package com.bb.fifteen.domain.record.repository;

import com.bb.fifteen.domain.record.entity.Season;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long> {
    @EntityGraph(attributePaths = {"stages"})
    @Query("select s from Season s")
    List<Season> findAllWithStages();

    @EntityGraph(attributePaths = {"stages"})
    @Query("select s from Season s where s.id = :id")
    Optional<Season> findByIdWithStages(@Param("id") Long id);

}
