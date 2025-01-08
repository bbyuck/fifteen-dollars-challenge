package com.bb.fifteen.domain.record.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * 시즌별 팀 Entity
 */
@Getter
@Entity
@Table(name = "teams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "season_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Season season;          // 시즌별 팀을 구분하기 위한 시즌 정보

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "team")
    private List<TeamRecord> records = new ArrayList<>();
}
