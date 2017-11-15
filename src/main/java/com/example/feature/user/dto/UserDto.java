package com.example.feature.user.dto;

import com.example.feature.base.dto.BaseModifiableDto;
import com.example.feature.user.dto.generated.ImmutableUserDto;
import com.example.feature.user.entity.QUserEntity;
import com.example.feature.user.entity.UserEntity;
import com.google.common.base.Preconditions;
import com.querydsl.core.Tuple;
import org.immutables.value.Value;

import javax.annotation.Nonnull;

@Value.Immutable
public interface UserDto extends BaseModifiableDto {

    @Nonnull String getUsername();

    // Link builder chain for UserDto -> BaseModifiableDto -> BaseUnmodifiableDto
    interface Builder extends BaseModifiableDto.ModifiableBuilder<UserDto, UserEntity> {}

    @Nonnull
    static UserDto entity(@Nonnull final UserEntity user) {
        Preconditions.checkNotNull(user);
        return ImmutableUserDto
                .builder()
                .username(user.getUsername())
                .modifiableEntityToDto(user);
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    static UserDto row(@Nonnull final Tuple row,
                       @Nonnull final QUserEntity user) {
        Preconditions.checkNotNull(row);
        return ImmutableUserDto
                .builder()
                .username(row.get(user.username))
                .modifiableRowToDto(row, user._super);
    }
}
