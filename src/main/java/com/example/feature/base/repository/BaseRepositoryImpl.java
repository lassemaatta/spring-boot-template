package com.example.feature.base.repository;

import com.example.feature.base.dto.BaseUnmodifiableDto;
import com.example.feature.base.entity.BaseEntity;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.NoRepositoryBean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import java.io.Serializable;

@NoRepositoryBean
public class BaseRepositoryImpl<ID extends Serializable, E extends BaseEntity, DTO extends BaseUnmodifiableDto>
        extends QueryDslJpaRepository<E, ID>
        implements BaseRepository<ID, E, DTO> {

    @Nonnull private final EntityPath<E> path;
    @Nonnull private final Querydsl querydsl;
    @Nonnull private final PathBuilder<E> pathBuilder;
    @Nonnull private final JPAQueryFactory factory;

    public BaseRepositoryImpl(@Nonnull final JpaEntityInformation<E, ID> entityInformation,
                              @Nonnull final EntityManager entityManager) {
        super(entityInformation, entityManager);
        path = SimpleEntityPathResolver.INSTANCE.createPath(entityInformation.getJavaType());
        pathBuilder = new PathBuilder<>(path.getType(), path.getMetadata());
        querydsl = new Querydsl(entityManager, pathBuilder);
        factory = new JPAQueryFactory(entityManager);
    }

    @Nonnull
    public PathBuilder<E> getPathBuilder() {
        return pathBuilder;
    }

    @Override
    @Nonnull
    public JPQLQuery<?> initializeQuery(@Nullable final Predicate pred) {
        return createQuery(pred);
    }

    @Override
    @Nonnull
    public JPQLQuery<E> toEntities(@Nonnull final JPQLQuery<?> query) {
        return query.select(path);
    }

    @Override
    @Nonnull
    public <T> JPQLQuery<T> applyPagination(@Nonnull final JPQLQuery<T> query,
                                            @Nullable final Pageable page) {
        return querydsl.applyPagination(page, query);
    }

    @Override
    @Nonnull
    public <T> JPQLQuery<T> applySorting(@Nonnull final JPQLQuery<T> query,
                                         @Nullable final Sort sort) {
        return querydsl.applySorting(sort, query);
    }
}
