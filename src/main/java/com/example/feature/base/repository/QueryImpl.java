package com.example.feature.base.repository;

import com.example.feature.base.dto.BaseDtoFactory;
import com.example.feature.base.dto.BaseUnmodifiableDto;
import com.example.feature.base.entity.BaseEntity;
import com.example.feature.base.entity.QBaseEntity;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QSort;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Objects;

public final class QueryImpl<ID extends Serializable, E extends BaseEntity, DTO extends BaseUnmodifiableDto>
        implements Query.Filtering<ID, E, DTO>, Query.Ordering<ID, E, DTO>, Query.ReturnType<ID, E, DTO>, Query.Limit<ID, E, DTO> {

    @Nonnull
    private final BaseRepository<ID, E, DTO> repository;

    @Nullable
    private Long limit;

    @Nullable
    private Pageable page;

    @Nullable
    private QSort sort;

    @Nullable
    private BaseDtoFactory<DTO> factory;

    @Nullable
    private Predicate predicate;

    private final Query.EntityResult<ID, E> entityBuilder = new EntityBuilder();
    private final Query.DtoResult<ID, DTO> dtoBuilder = new DtoBuilder();

    QueryImpl(final BaseRepository<ID, E, DTO> repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    // Query.Filtering API:

    @Nonnull
    @Override
    public Query.Ordering<ID, E, DTO> noFilter() {
        return this;
    }


    @Nonnull
    @Override
    public Query.Ordering<ID, E, DTO> filter(@Nonnull final Predicate predicate) {
        this.predicate = Objects.requireNonNull(predicate);
        return this;
    }

    // Query.Ordering API:

    @Nonnull
    @Override
    public Query.Limit<ID, E, DTO> orderById() {
        sort = new QSort(repository.getPathBuilder().get(QBaseEntity.baseEntity.id).asc());
        return this;
    }

    @Nonnull
    @Override
    public Query.Limit<ID, E, DTO> orderBy(@Nonnull final OrderSpecifier<?>... orders) {
        sort = new QSort(Objects.requireNonNull(orders));
        return this;
    }

    // Query.Limit API:

    @Nonnull
    @Override
    public Query.ReturnType<ID, E, DTO> noLimit() {
        return this;
    }


    @Nonnull
    public Query.ReturnType<ID, E, DTO> limit(final long limit) {
        this.limit = limit;
        return this;
    }

    // Query.ReturnType API:

    @Nonnull
    @Override
    public Query.EntityResult<ID, E> entity() {
        return entityBuilder;
    }

    @Nonnull
    public Query.DtoResult<ID, DTO> dto(@Nonnull final BaseDtoFactory<DTO> factory) {
        this.factory = Objects.requireNonNull(factory);
        return dtoBuilder;
    }

    // Query Entity/DTO Result API with helper methods:

    @Nonnull
    private <T> JPQLQuery<T> applyLimit(@Nonnull final JPQLQuery<T> query) {
        return (limit == null) ? query : query.limit(limit);
    }

    @Nonnull
    private <T> Page<T> toPage(@Nonnull final JPQLQuery<T> query) {
        final QueryResults<T> results = query.fetchResults();
        return new PageImpl<>(results.getResults(), page, results.getTotal());
    }

    @Nonnull
    private static <T> ImmutableList<T> toList(@Nonnull final JPQLQuery<T> query) {
        return ImmutableList.copyOf(query.fetchResults().getResults());
    }

    @Nonnull
    private Pageable preprocessPage(@Nonnull final Pageable page) {
        Preconditions.checkNotNull(page);
        Preconditions.checkNotNull(sort);

        final int pageNumber = page.getPageNumber();
        final int pageSize;
        if ((limit != null) && (limit < (long) page.getPageSize())) {
            pageSize = limit.intValue();
        } else {
            pageSize = page.getPageSize();
        }
        return new QPageRequest(pageNumber, pageSize, sort);
    }

    private class EntityBuilder implements Query.EntityResult<ID, E> {

        @Nonnull
        @Override
        @Transactional
        public Page<E> page(@Nonnull final Pageable pageable) {
            Preconditions.checkNotNull(pageable);
            page = preprocessPage(pageable);
            return toPage(
                    repository.applyPagination(
                            repository.toEntities(
                                    repository.initializeQuery(predicate)
                            ), page
                    )
            );
        }

        @Nonnull
        @Override
        @Transactional
        public ImmutableList<E> list() {
            return toList(
                    applyLimit(
                            repository.applySorting(
                                    repository.toEntities(
                                            repository.initializeQuery(predicate)
                                    ), sort
                            )
                    )
            );
        }
    }

    private class DtoBuilder implements Query.DtoResult<ID, DTO> {

        @Nonnull
        @Override
        @Transactional
        public Page<DTO> page(@Nonnull final Pageable pageable) {
            Preconditions.checkNotNull(pageable);
            Preconditions.checkNotNull(factory);
            page = preprocessPage(pageable);
            return toPage(
                    repository.applyPagination(
                            factory.build(
                                    repository.initializeQuery(predicate)
                            ), page
                    )
            );
        }

        @Nonnull
        @Override
        @Transactional
        public ImmutableList<DTO> list() {
            Preconditions.checkNotNull(factory);
            return toList(
                    applyLimit(
                            repository.applySorting(
                                    factory.build(
                                            repository.initializeQuery(predicate)
                                    ), sort
                            )
                    )
            );
        }
    }
}
