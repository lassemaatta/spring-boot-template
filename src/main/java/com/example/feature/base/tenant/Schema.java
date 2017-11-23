package com.example.feature.base.tenant;

import com.example.feature.base.tenant.generated.ImmutableSchema;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

@Immutable
public abstract class Schema {

    private static final String PREFIX = "APP_";
    private static final Pattern COMPILE = Pattern.compile(PREFIX);

    public static final Schema DEFAULT_SCHEMA = of(Tenant.DEFAULT_TENANT);

    @Parameter
    protected abstract Tenant tenant();

    @Nonnull
    public static Schema of(@Nonnull final Tenant tenant) {
        return ImmutableSchema.builder().tenant(tenant).build();
    }

    @Nullable
    public static Schema of(@Nonnull final String schemaName) {
        final String schema = schemaName.toUpperCase();
        if (schema.startsWith(PREFIX)) {
            return ImmutableSchema
                    .builder()
                    .tenant(Tenant.of(COMPILE.matcher(schema).replaceFirst("")))
                    .build();
        }
        return null;
    }

    @Derived
    protected String schemaName() {
        return PREFIX + tenant().toString().toUpperCase();
    }

    @Override
    public String toString() {
        return schemaName();
    }
}
