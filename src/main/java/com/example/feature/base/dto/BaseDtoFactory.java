package com.example.feature.base.dto;

import com.example.feature.base.entity.QBaseEntity;
import com.example.feature.base.entity.QModifiableEntity;
import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.MappingProjection;
import com.querydsl.jpa.JPQLQuery;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BaseDtoFactory<T extends BaseUnmodifiableDto> extends MappingProjection<T> {

    @Nonnull
    protected static List<Expression<?>> getBaseFields(@Nonnull final QBaseEntity entity) {
        return ImmutableList.of(
                entity.id,
                entity.creationTime
        );
    }

    @Nonnull
    protected static List<Expression<?>> getVersionFields(@Nonnull final QModifiableEntity entity) {
        return ImmutableList.of(
                entity.version,
                entity.modificationTime,
                entity.deletionTime
        );
    }

    protected BaseDtoFactory(final Class<? super T> type, @Nonnull final List<Expression<?>> args) {
        super(type, args.toArray(new Expression[args.size()]));
    }

    /**
     * Construct a query with type T with necessary selects and joins
     *
     * @param   query
     *          An unbound wildcard query
     * @return  A typed query with correct selects and joins
     */
    @Nonnull
    public abstract JPQLQuery<T> build(@Nonnull JPQLQuery<?> query);
}
