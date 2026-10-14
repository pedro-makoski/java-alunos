package com.alunos.scholl.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter

public class BaseEntity {
    @CreatedDate
    Instant createdAt;

    @LastModifiedDate
    Instant updatedAt;

    Boolean isDeleted;

    Instant deletedAt;
}
