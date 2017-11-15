package com.example.feature.base.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.io.Serializable;

@NoRepositoryBean
public class BaseRepositoryImpl<T, ID extends Serializable>
        extends QueryDslJpaRepository<T, ID>
        implements BaseRepository<T, ID> {

    public BaseRepositoryImpl(@Nonnull final JpaEntityInformation<T, ID> entityInformation,
                              @Nonnull final EntityManager entityManager) {
        super(entityInformation, entityManager);
    }
}
