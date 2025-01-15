package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.domain.record.code.ResultCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 팀별 기록을 나타냄 - 시즌별 팀으로도 분리해서 집계 가능하도록 구성된 Entity
 */
@Getter
@Entity
@Table(name = "team_records")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamRecord {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_team_id")
    private SeasonTeam seasonTeam;              // 기록을 소유한 팀

    @Enumerated(EnumType.STRING)
    @Column(name = "result_code")
    private ResultCode resultCode;  // 결과 코드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;              // 기록한 경기

    @Column(name = "tower")
    private int tower;              // 파괴한 타워
    
    @Column(name = "drake")
    private int drake;              // 처치한 용
    
    @Column(name = "baron")
    private int baron;              // 처치한 바론
}
