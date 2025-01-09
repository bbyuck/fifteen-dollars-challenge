package com.bb.fifteen.domain.record.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 시즌별 코치 정보를 나타내는 Entity
 */
@Getter
@Entity
@Table(name = "coaches")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coach {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;              // 소속팀 -> 시즌별 팀으로 Entity는 시즌별 코치 정보를 나타냄

    @Column(name = "summoner_name")
    private String summonerName;    // 소환사명

    @Column(name = "name")
    private String name;            // 이름
}
