package com.bb.fifteen.domain.record.repository;

import com.bb.fifteen.domain.record.entity.Player;
import com.bb.fifteen.domain.record.repository.custom.CustomPlayerRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long>, CustomPlayerRepository {

}
