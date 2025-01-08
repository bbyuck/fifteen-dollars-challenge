package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.domain.record.code.LeagueCode;
import com.bb.fifteen.domain.record.code.StageCode;
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
    @JoinColumn(name = "team_id")
    private Team team;              // 기록을 소유한 팀

    @Enumerated
    @Column(name = "league_code")
    private LeagueCode leagueCode;  // 기록을 남긴 리그의 코드

    @Enumerated
    @Column(name = "stage_code")
    private StageCode stageCode;    // 기록을 남긴 라운드

}
