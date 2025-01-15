package com.bb.fifteen.domain.record.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "season_team_player_mapping")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeasonTeamPlayerMapping {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_team_id")
    private SeasonTeam seasonTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_player_id")
    private SeasonPlayer seasonPlayer;

}
