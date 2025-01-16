package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.domain.record.code.SourceDomainCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 선수 기준 정보 Entity
 */
@Getter
@Entity
@Table(name = "players")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player extends RecordBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "kor_name")
    private String korName;     // 선수 이름

    @Column(name = "eng_name")
    private String engName;     // 선수 영어이름

    public Player(String korName, String engName) {
        this.korName = korName;
        this.engName = engName;
    }

    public Player(String korName, String engName, Long sourceId, SourceDomainCode sourceDomain) {
        this(korName, engName);
        this.sourceId = sourceId;
        this.sourceDomain = sourceDomain;
    }
}
