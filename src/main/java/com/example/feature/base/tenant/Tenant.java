package com.example.feature.base.tenant;

import com.example.feature.base.tenant.generated.ImmutableTenant;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

@Immutable
public abstract class Tenant {

    private static final Pattern VALID_TENANT = Pattern.compile("[a-z][a-z_]{1,20}");

    public static final Tenant DEFAULT_TENANT = of("default");

    @Parameter
    protected abstract String value();

    @Check
    public void check() {
        if (!VALID_TENANT.matcher(value()).matches()) {
            throw new IllegalArgumentException(String.format("'%s' is not a valid tenant name!", value()));
        }
    }

    @Nonnull
    public static Tenant of(@Nonnull final String tenantName) {
        return ImmutableTenant
                .builder()
                .value(tenantName.toLowerCase())
                .build();
    }

    @Override
    public String toString() {
        return value();
    }
}
