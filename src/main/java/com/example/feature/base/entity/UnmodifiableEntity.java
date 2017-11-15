package com.example.feature.base.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class UnmodifiableEntity extends BaseEntity {

    @PreUpdate
    protected void preUpdate() {
        throw new UnsupportedOperationException("UnmodifiableEntity cannot be updated!");
    }
}
