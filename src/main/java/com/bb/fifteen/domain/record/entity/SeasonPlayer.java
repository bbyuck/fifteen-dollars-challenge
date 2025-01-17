package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.common.BaseEntity;
import com.bb.fifteen.domain.record.code.PositionCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Season season;              // 해당 기록 Entity가 존재하는 시즌

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;              // 해당 기록을 세운 선수 기준 정보 Entity

    @Column(name = "summoner_name")
    private String summonerName;        // 소환사명

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private PositionCode position;      // 포지션

    @OneToMany(mappedBy = "seasonPlayer")
    private List<PlayerRecord> records = new ArrayList<>();

    @OneToMany(mappedBy = "seasonPlayer")
    private List<SeasonTeamPlayerMapping> seasonTeamPlayerMappings = new ArrayList<>();

    public SeasonPlayer(Season season, Player player, String summonerName, PositionCode position) {
        this.season = season;
        this.player = player;
        this.summonerName = summonerName;
        this.position = position;
    }
}
