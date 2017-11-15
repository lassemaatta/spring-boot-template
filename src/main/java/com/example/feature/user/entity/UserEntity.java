package com.example.feature.user.entity;

import com.example.feature.base.entity.ModifiableEntity;
import com.google.common.base.Preconditions;

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

    private UserEntity() {}

    @Nonnull
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nonnull final String username) {
        this.username = Objects.requireNonNull(username);
    }

    @Nonnull
    public static UserEntity create(@Nonnull final String username) {
        Preconditions.checkNotNull(username);

        final UserEntity user = new UserEntity();
        user.setUsername(username);
        return user;
    }
}
