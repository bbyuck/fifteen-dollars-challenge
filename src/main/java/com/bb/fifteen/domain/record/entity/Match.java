package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.domain.record.code.LeagueCode;
import com.bb.fifteen.domain.record.code.SeasonCode;
import com.bb.fifteen.domain.record.code.StageCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 매치 기록 Entity
 *
 * 매치 기록 하나에 1개 이상의 게임 Entity (단판제 + 다전제)
 * 매치 기록 하나에 team 2개
 */
@Getter
@Entity
@Table(name = "matches")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "league_code")
    private LeagueCode leagueCode;  // 매치를 진행한 리그의 코드

    @Enumerated(EnumType.STRING)
    @Column(name = "season_code")
    private SeasonCode seasonCode;  // 매치를 진행한 시즌 코드

    @Enumerated(EnumType.STRING)
    private StageCode stageCode;    // 매치

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;          // 매치를 진행한 홈팀

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;          // 매치를 진행한 어웨이팀

}
