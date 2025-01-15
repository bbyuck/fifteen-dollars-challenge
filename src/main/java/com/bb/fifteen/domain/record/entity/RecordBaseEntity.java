package com.bb.fifteen.domain.record.entity;

import com.bb.fifteen.common.BaseEntity;
import com.bb.fifteen.domain.record.code.SourceDomainCode;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class RecordBaseEntity extends BaseEntity {

    @Column(name = "source_id")
    protected Long sourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_domain")
    protected SourceDomainCode sourceDomain;
}
