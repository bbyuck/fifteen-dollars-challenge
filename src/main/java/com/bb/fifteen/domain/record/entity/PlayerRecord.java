package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.common.BaseEntity;
import com.bb.fifteen.domain.record.code.LeagueCode;
import com.bb.fifteen.domain.record.code.StageCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 선수 커리어의 세부 기록을 나타내는 Entity
 */
@Getter
@Entity
@Table(name = "player_records")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerRecord extends RecordBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_player_id")
    private SeasonPlayer seasonPlayer;          // 기록을 소유한 선수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;              // 플레이한 게임

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "champion_id")
    private Champion champion;      // 플레이한 챔피언

    @Column(name = "kill")
    private int kill;               // 해당 경기에서 기록한 킬

    @Column(name = "death")
    private int death;              // 해당 경기에서 기록한 데스

    @Column(name = "assist")
    private int assist;             // 해당 경기에서 기록한 어시스트

    @Column(name = "gold")
    private int gold;               // Gold 수급량

    @Column(name = "cs")
    private int cs;                 // CS 수급량

}
