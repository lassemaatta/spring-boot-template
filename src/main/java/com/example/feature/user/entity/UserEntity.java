package com.example.feature.user.entity;

import com.example.feature.base.entity.ModifiableEntity;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity(name = "users")
public class UserEntity extends ModifiableEntity {

    @NotNull
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Nonnull
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nonnull final String username) {
        this.username = Objects.requireNonNull(username);
    }
}
