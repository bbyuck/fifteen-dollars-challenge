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

    @Enumerated
    @Column(name = "code")
    private SeasonCode code;

    @Column(name = "year")
    private int year;

}
