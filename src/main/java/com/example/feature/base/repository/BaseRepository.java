package com.example.feature.base.repository;

import com.example.feature.base.dto.BaseUnmodifiableDto;
import com.example.feature.base.entity.BaseEntity;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<ID extends Serializable, E extends BaseEntity, DTO extends BaseUnmodifiableDto>
        extends JpaRepository<E, ID>, QueryDslPredicateExecutor<E> {

    /**
     * Retrieve the QueryDSL path builder
     * @apiNote Internal API needed by {@link QueryImpl}
     * @return  The QueryDSL PathBuilder for the associated BaseEntity class
     */
    @Nonnull
    PathBuilder<E> getPathBuilder();

    /**
     * Initialize a JPQL query based on an initial predicate
     * @apiNote Internal API needed by {@link QueryImpl}
     * @param   pred
     *          The optional initial predicate
     * @return  The generic JPQL query
     */
    @Nonnull
    JPQLQuery<?> initializeQuery(@Nullable Predicate pred);

    /**
     * Configurea generic query to return entities
     * @apiNote Internal API needed by {@link QueryImpl}
     * @param   query
     *          A generic JPQL query
     * @return  A JPQL query which returns BaseEntity-rows
     */
    @Nonnull
    JPQLQuery<E> toEntities(@Nonnull JPQLQuery<?> query);

    /**
     * Apply pagination requirements (e.g. limit) on a query
     * @apiNote Internal API needed by {@link QueryImpl}
     * @param   query
     *          The target non-generic query
     * @param   page
     *          The required paging parameters
     * @param   <T>
     *          The query generic type
     * @return  A non-generic query with paging limits set
     */
    @Nonnull
    <T> JPQLQuery<T> applyPagination(@Nonnull JPQLQuery<T> query,
                                     @Nullable Pageable page);

    /**
     * Apply sorting order on a query
     * @apiNote Internal API needed by {@link QueryImpl}
     * @param   query
     *          The target non-generic query
     * @param   sort
     *          The optional sorting order
     * @param   <T>
     *          The query generic type
     * @return  A non-generic query with sorting order set
     */
    @Nonnull
    <T> JPQLQuery<T> applySorting(@Nonnull JPQLQuery<T> query,
                                  @Nullable Sort sort);
}
