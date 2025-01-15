package com.bb.fifteen.domain.record.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 세트 경기 기록 Entity
 * 한 경기 당  팀 기록 Entity 2개
 *            선수 기록 Entity 10개
 */
@Getter
@Entity
@Table(name = "games")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game extends RecordBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;    // 경기를 진행한 매치

    @Column(name = "time")
    private int time;       // 경기 시간
}
