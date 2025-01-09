package com.bb.fifteen.domain.record.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "champions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Champion {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "english_name")
    private String englishName;     // 챔피언 영어명

    @Column(name = "korean_name")
    private String koreanName;      // 챔피언 한글명
}
