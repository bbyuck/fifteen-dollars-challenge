package com.bb.fifteen.domain.record.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 선수 기준 정보 Entity
 */
@Getter
@Entity
@Table(name = "players")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player extends RecordBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "season_team_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SeasonTeam team;  // 선수가 속한 팀 -> 시즌별 팀

    @Column(name = "summoner_name")
    private String summonerName;

    @Column(name = "name")
    private String name;    // 선수 이름

}
