package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.domain.record.code.SeasonCode;
import com.bb.fifteen.domain.record.code.SourceDomainCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * 시즌 정보를 나타내는 기준정보 Entity
 */
@Getter
@Entity
@Table(name = "seasons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Season extends RecordBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "season_year")
    private int year;               // 연도

    @Enumerated(EnumType.STRING)
    @Column(name = "code")
    private SeasonCode code;        // 연도내의 시즌 코드

    @OneToMany(mappedBy = "season")
    private List<Stage> stages = new ArrayList<>();

    public Season(int year, SeasonCode code) {
        this.year = year;
        this.code = code;
    }

    public Season(int year, SeasonCode code, Long sourceId, SourceDomainCode sourceDomain) {
        this(year, code);
        this.sourceId = sourceId;
        this.sourceDomain = sourceDomain;
    }

    public void add(Stage stage) {
        this.stages.add(stage);
        stage.includeIn(this);
    }
    public void addAll(List<Stage> stages) {
        stages.forEach(stage -> stage.includeIn(this));
        this.stages.addAll(stages);
    }
}
