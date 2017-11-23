package com.example.feature.base.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class H2Condition implements Condition {

    private static final String H2 = "jdbc:h2";

    @Override
    public boolean matches(final ConditionContext context,
                           final AnnotatedTypeMetadata metadata) {
        return context
                .getEnvironment()
                .getRequiredProperty("spring.datasource.url")
                .startsWith(H2);
    }
}
