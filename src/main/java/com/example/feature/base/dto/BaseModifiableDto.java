package com.example.feature.base.dto;

import com.example.feature.base.entity.ModifiableEntity;
import com.example.feature.base.entity.QModifiableEntity;
import com.google.common.base.Preconditions;
import com.querydsl.core.Tuple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;

public interface BaseModifiableDto extends BaseUnmodifiableDto {

    int getVersion();

    @Nonnull
    Instant getModificationTime();

    @Nullable
    Instant getDeletionTime();

    /**
     * A generic builder for DTOs based on {@link ModifiableEntity} entities. The child classes may use this builder
     * to delegate the construction of the base fields.
     * @param   <T>
     *          The concrete type of the DTO
     * @param   <E>
     *          The concrete type of the corresponding {@link ModifiableEntity}
     */
    interface ModifiableBuilder<T extends BaseModifiableDto, E extends ModifiableEntity>
            extends BaseUnmodifiableDto.UnmodifiableBuilder<T, E> {

        BaseModifiableDto.ModifiableBuilder<T, E> version(int version);
        BaseModifiableDto.ModifiableBuilder<T, E> modificationTime(@Nonnull Instant modificationTime);
        BaseModifiableDto.ModifiableBuilder<T, E> deletionTime(@Nullable Instant deletionTime);

        /**
         * Utility builder for noFilter modifiable entity -based DTOs.
         * @param   e
         *          The source entity
         * @return  The built DTO
         */
        @Nonnull
        default T modifiableEntityToDto(@Nonnull final E e) {
            Preconditions.checkNotNull(e);
            Preconditions.checkNotNull(e.getVersion());
            return version(e.getVersion())
                    .modificationTime(e.getModificationTime())
                    .deletionTime(e.getDeletionTime())
                    .baseEntityToDto(e);
        }

        @SuppressWarnings("ConstantConditions")
        @Nonnull
        default T modifiableRowToDto(@Nonnull final Tuple row,
                                     @Nonnull final QModifiableEntity entity) {
            Preconditions.checkNotNull(row);
            return version(row.get(entity.version))
                    .modificationTime(row.get(entity.modificationTime))
                    .deletionTime(row.get(entity.deletionTime))
                    .baseRowToDto(row, entity._super);
        }
    }
}
