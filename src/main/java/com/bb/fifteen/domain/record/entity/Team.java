package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.domain.record.code.SourceDomainCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 시즌별 팀 Entity
 */
@Getter
@Entity
@Table(name = "teams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends RecordBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "founded_year")
    private int foundedYear;        // 팀 설립 연도

    @Column(name = "disbanded_year")
    private int disbandedYear;     // 팀 해체 연도

    public Team(int foundedYear, int disbandedYear) {
        this.foundedYear = foundedYear;
        this.disbandedYear = disbandedYear;
    }

    public Team(int foundedYear, int disbandedYear, Long sourceId, SourceDomainCode sourceDomain) {
        this(foundedYear, disbandedYear);
        this.sourceId = sourceId;
        this.sourceDomain = sourceDomain;
    }
}
