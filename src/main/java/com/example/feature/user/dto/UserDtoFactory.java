package com.example.feature.user.dto;

import com.example.feature.base.dto.BaseDtoFactory;
import com.example.feature.user.entity.QUserEntity;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPQLQuery;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class UserDtoFactory extends BaseDtoFactory<UserDto> {

    private static final QUserEntity USER = QUserEntity.userEntity;

    private static final List<Expression<?>> FIELDS =
            ImmutableList.<Expression<?>>builder()
                    .addAll(getBaseFields(USER._super._super))
                    .addAll(getVersionFields(USER._super))
                    .add(USER.username)
                    .build();

    public UserDtoFactory() {
        super(UserDto.class, FIELDS);
    }

    @Override
    protected UserDto map(@Nonnull final Tuple row) {
        return map(row, USER);
    }

    @Nullable
    public static UserDto map(@Nonnull final Tuple row,
                              @Nullable final QUserEntity user) {
        Preconditions.checkNotNull(row);
        return (user == null) ? null : UserDto.row(row, user);
    }

    @Nonnull
    @Override
    public JPQLQuery<UserDto> build(@Nonnull final JPQLQuery<?> query) {
        return query.select(this);
    }
}
