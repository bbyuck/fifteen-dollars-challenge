package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.common.BaseEntity;
import com.bb.fifteen.domain.record.code.PositionCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "season_player")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeasonPlayer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private PositionCode position;

    @OneToMany(mappedBy = "seasonPlayer")
    private List<PlayerRecord> records = new ArrayList<>();
}
