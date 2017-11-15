package com.example.feature.base.dto;

import com.example.feature.base.entity.BaseEntity;
import com.example.feature.base.entity.QBaseEntity;
import com.google.common.base.Preconditions;
import com.querydsl.core.Tuple;

import javax.annotation.Nonnull;
import java.time.Instant;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface BaseUnmodifiableDto {
    long getId();

    @Nonnull
    Instant getCreationTime();

    /**
     * A generic builder for DTOs based on {@link BaseEntity} entities. The child classes may use this builder
     * to delegate the construction of the base fields.
     * @param   <T>
     *          The concrete type of the DTO
     * @param   <E>
     *          The concrete type of the corresponding BaseEntity
     */
    interface UnmodifiableBuilder<T extends BaseUnmodifiableDto, E extends BaseEntity> {

        BaseUnmodifiableDto.UnmodifiableBuilder<T, E> id(long id);
        BaseUnmodifiableDto.UnmodifiableBuilder<T, E> creationTime(@Nonnull Instant creationTime);

        T build();

        /**
         * Build DTOs from Entities
         * @param   e
         *          The source entity
         * @return  The built DTO
         */
        @Nonnull
        default T baseEntityToDto(@Nonnull final E e) {
            Preconditions.checkNotNull(e);
            Preconditions.checkNotNull(e.getId());
            return id(e.getId())
                    .creationTime(e.getCreationTime())
                    .build();
        }

        /**
         * Build DTOs from QueryDSL row results
         * @param   row
         *          The source row
         * @param   entity
         *          The target QueryDSL path
         * @return  A built DTO
         */
        @SuppressWarnings("ConstantConditions")
        @Nonnull
        default T baseRowToDto(@Nonnull final Tuple row,
                               @Nonnull final QBaseEntity entity) {
            Preconditions.checkNotNull(row);
            return id(row.get(entity.id))
                    .creationTime(row.get(entity.creationTime))
                    .build();
        }
    }
}
