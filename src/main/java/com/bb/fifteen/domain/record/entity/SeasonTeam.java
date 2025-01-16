package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.common.BaseEntity;
import com.bb.fifteen.domain.record.code.SourceDomainCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "season_team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeasonTeam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "name")
    private String name;

    @Column(name = "initial_name")
    private String initialName;

    @OneToMany(mappedBy = "seasonTeam")
    private List<TeamRecord> records = new ArrayList<>();

    public SeasonTeam(Season season, Team team, String name, String initialName) {
        this.season = season;
        this.team = team;
        this.name = name;
        this.initialName = initialName;
    }
}
