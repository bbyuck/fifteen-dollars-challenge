package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.domain.record.code.SeasonCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 시즌 정보를 나타내는 기준정보 Entity
 */
@Getter
@Entity
@Table(name = "seasons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Season {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code")
    private SeasonCode code;        // 연도내의 시즌 코드

    @Column(name = "year")
    private int year;               // 연도

}
