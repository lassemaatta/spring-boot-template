package com.example.feature.base.dto;

import org.immutables.value.Value;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Value.Style(
        // Don't require our abstract classes to start with the "Abstract"-prefix
        typeAbstract = "*",
        // Put generated code under a specific package for easy filtering
        packageGenerated = "*.generated",
        // Pass @Nonnull to the implementation to silence IDEA warnings
        passAnnotations = Nonnull.class,
        // Minimize generated code by:
        // a) we don't need from methods and we construct the DTOs in one go
        strictBuilder = true,
        // and b) we don't need copy methods
        defaults = @Value.Immutable(copy = false)
)
public @interface Style {}
