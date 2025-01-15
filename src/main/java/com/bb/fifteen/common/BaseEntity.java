package com.bb.fifteen.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    protected LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    protected LocalDateTime updatedAt;

}
