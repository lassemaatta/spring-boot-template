package com.example.feature.base.entity;

import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * H2 seems to have issues storing Java 8 instants
 */
@Converter(autoApply = true)
public class InstantConverter implements AttributeConverter<Instant, Timestamp> {

    @Nullable
    @Override
    public Timestamp convertToDatabaseColumn(final Instant attribute) {
        return (attribute != null) ? new Timestamp(attribute.toEpochMilli()) : null;
    }

    @Nullable
    @Override
    public Instant convertToEntityAttribute(final Timestamp dbData) {
        return (dbData != null) ? dbData.toInstant() : null;
    }
}
