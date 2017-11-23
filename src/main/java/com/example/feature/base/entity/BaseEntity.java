package com.example.feature.base.entity;

import org.springframework.data.domain.Persistable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@MappedSuperclass
public abstract class BaseEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Column(name = "creation_time", nullable = false, updatable = false)
    private Instant creationTime;

    @Nullable
    @Override
    public Long getId() {
        return id;
    }

    @Nonnull
    public Instant getCreationTime() {
        return creationTime;
    }

    @PrePersist
    public void prePersistBaseEntity() {
        creationTime = Instant.now();
    }

    @Override
    @Transient
    public boolean isNew() {
        return id == null;
    }
}
