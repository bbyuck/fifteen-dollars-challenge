package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.domain.record.code.SourceDomainCode;
import com.bb.fifteen.domain.record.code.StageCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "round")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stage extends RecordBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @Enumerated(EnumType.STRING)
    @Column(name = "code")
    private StageCode code;

    public Stage(Season season, StageCode code) {
        this.code = code;
        includeIn(season);
    }

    public Stage(Season season, StageCode code, Long sourceId, SourceDomainCode sourceDomain) {
        this(season, code);
        this.sourceId = sourceId;
        this.sourceDomain = sourceDomain;
    }

    public void includeIn(Season season) {
        this.season = season;
    }
}
