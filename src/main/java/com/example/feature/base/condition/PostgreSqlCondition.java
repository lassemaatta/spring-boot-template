package com.example.feature.base.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class PostgreSqlCondition implements Condition {

    private static final String POSTGRES = "jdbc:postgresql";

    @Override
    public boolean matches(final ConditionContext context,
                           final AnnotatedTypeMetadata metadata) {
        return context
                .getEnvironment()
                .getRequiredProperty("spring.datasource.url")
                .startsWith(POSTGRES);
    }
}
