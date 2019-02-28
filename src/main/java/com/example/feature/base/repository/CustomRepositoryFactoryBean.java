package com.example.feature.base.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.NonNull;

import javax.persistence.EntityManager;


public class CustomRepositoryFactoryBean<R extends Repository<E, ID>, E, ID>
        extends JpaRepositoryFactoryBean<R, E, ID> {

    public CustomRepositoryFactoryBean(final Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @NonNull
    protected RepositoryFactorySupport createRepositoryFactory(final EntityManager entityManager) {
        return new CustomRepositoryFactory(entityManager);
    }
}
