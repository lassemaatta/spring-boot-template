package com.example.feature.base.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class ModifiableEntity
        extends BaseEntity {

    @Version
    @NotNull
    @Column(nullable = false)
    private Integer version;

    @NotNull
    @Column(name = "modification_time", nullable = false)
    private Instant modificationTime;

    @Nullable
    @Column(name = "deletion_time")
    private Instant deletionTime;

    @PrePersist
    public void prePersistModifiableEntity() {
        modificationTime = isNew() ? getCreationTime() : Instant.now();
    }

    @PreUpdate
    public void preUpdateModifiableEntity() {
        modificationTime = Instant.now();
    }

    @Nullable
    public Integer getVersion() {
        return version;
    }

    @Nonnull
    public Instant getModificationTime() {
        return modificationTime;
    }

    @Nullable
    public Instant getDeletionTime() {
        return deletionTime;
    }

    public boolean isDeleted() {
        return deletionTime != null;
    }
}
