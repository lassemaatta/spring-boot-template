package com.example.feature.base.repository;

import com.example.feature.base.dto.BaseDtoFactory;
import com.example.feature.base.dto.BaseUnmodifiableDto;
import com.example.feature.base.entity.BaseEntity;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.Nonnull;
import java.io.Serializable;

public interface Query {

    /**
     * Begin construction of a query against the given repository
     * @param   repository
     *          The source repository
     * @param   <ID>
     *          The primary key type, e.g. {@link Long}
     * @param   <E>
     *          The {@link BaseEntity} type of the repository
     * @param   <DTO>
     *          The {@link BaseUnmodifiableDto} type of the repository
     * @return  The next stage of the query builder
     */
    static <ID extends Serializable, E extends BaseEntity, DTO extends BaseUnmodifiableDto> Query.Filtering<ID, E, DTO> of(@Nonnull final BaseRepository<ID, E, DTO> repository) {
        Preconditions.checkNotNull(repository);
        return new QueryImpl<>(repository);
    }

    interface Filtering<ID extends Serializable, E extends BaseEntity, DTO extends BaseUnmodifiableDto> {
        /**
         * Perform no filtering on the source tables
         * @return  The next stage of the query builder
         */
        @Nonnull Query.Ordering<ID, E, DTO> noFilter();

        /**
         * Filter the source tables with the given predicate
         * @param   predicate
         *          Predicate to use to filter the query results
         * @return  The next stage of the query builder
         */
        @Nonnull Query.Ordering<ID, E, DTO> filter(@Nonnull Predicate predicate);
    }

    interface Ordering<ID extends Serializable, E extends BaseEntity, DTO extends BaseUnmodifiableDto> {
        /**
         * Order the rows according to the primary key
         * @return  The next stage of the query builder
         */
        @Nonnull Query.Limit<ID, E, DTO> orderById();

        /**
         * Order the rows according to the given column(s)
         * @param   orders
         *          The columns to order by
         * @return  The next stage of the query builder
         */
        @Nonnull Query.Limit<ID, E, DTO> orderBy(@Nonnull OrderSpecifier<?>... orders);
    }

    interface Limit<ID extends Serializable, E extends BaseEntity, DTO extends BaseUnmodifiableDto> {
        /**
         * Do not limit the number of returned rows
         * @return  The next stage of the query builder
         */
        @Nonnull Query.ReturnType<ID, E, DTO> noLimit();

        /**
         * Limit the number of returned rows
         * @param   limit
         *          Maximum number of rows to return
         * @return  The next stage of the query builder
         */
        @Nonnull Query.ReturnType<ID, E, DTO> limit(long limit);
    }

    interface ReturnType<ID extends Serializable, E extends BaseEntity, DTO extends BaseUnmodifiableDto> {
        /**
         * Return the results as entities
         * @return  The next stage of the query builder
         */
        @Nonnull Query.EntityResult<ID, E> entity();

        /**
         * Return the results as DTOs
         * @param   factory
         *          Use the given factory to transform result rows to DTOs
         * @return  The next stage of the query builder
         */
        @Nonnull Query.DtoResult<ID, DTO> dto(@Nonnull BaseDtoFactory<DTO> factory);
    }

    interface EntityResult<ID extends Serializable, E extends BaseEntity> {
        /**
         * Execute the query and return the results
         * @param   pageable
         *          Paging information used to retrieve the correct page
         * @apiNote The resulting page may contain fewer results than requested
         *          if limit() has been specified and is smaller than the page size
         * @return  The query results as a page of entities
         */
        @Nonnull Page<E> page(@Nonnull Pageable pageable);

        /**
         * Execute the query and return the results
         * @return  The query results as a list of entities
         */
        @Nonnull ImmutableList<E> list();
    }

    interface DtoResult<ID extends Serializable, DTO extends BaseUnmodifiableDto> {
        /**
         * Execute the query and return the results
         * @param   pageable
         *          Paging information used to retrieve the correct page
         * @apiNote The resulting page may contain fewer results than requested
         *          if limit() has been specified and is smaller than the page size
         * @return  The query results as a page of DTOs
         */
        @Nonnull Page<DTO> page(@Nonnull Pageable pageable);

        /**
         * Execute the query and return the results
         * @return  The query results as a list of DTOs
         */
        @Nonnull ImmutableList<DTO> list();
    }
}
