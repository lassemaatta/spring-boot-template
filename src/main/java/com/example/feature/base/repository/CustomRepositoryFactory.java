package com.example.feature.base.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition.RepositoryFragments;
import org.springframework.data.repository.core.support.RepositoryFragment;
import org.springframework.lang.NonNull;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.io.Serializable;

public class CustomRepositoryFactory extends JpaRepositoryFactory {

    private final EntityManager entityManager;

    CustomRepositoryFactory(final EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @NonNull
    protected RepositoryFragments getRepositoryFragments(@Nonnull final RepositoryMetadata metadata) {
        final RepositoryFragments fragments = super.getRepositoryFragments(metadata);

        if (QueryRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {

            final JpaEntityInformation<?, Serializable> entityInformation =
                    getEntityInformation(metadata.getDomainType());

            final Object queryableFragment = getTargetRepositoryViaReflection(
                    QueryRepositoryImpl.class,
                    entityInformation,
                    entityManager);

            return fragments.append(RepositoryFragment.implemented(queryableFragment));
        }

        return fragments;
    }
}
