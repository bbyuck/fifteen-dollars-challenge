package com.bb.fifteen.domain.record.entity;

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
public class PlayerRecord {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;          // 기록을 소유한 선수

    @Enumerated
    @Column(name = "league_code")
    private LeagueCode leagueCode;  // 기록을 남긴 리그의 코드

    @Enumerated
    @Column(name = "stage_code")
    private StageCode stageCode;    // 기록을 남긴 라운드
}
