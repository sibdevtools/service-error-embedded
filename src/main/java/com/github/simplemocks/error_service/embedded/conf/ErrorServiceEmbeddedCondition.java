package com.github.simplemocks.error_service.embedded.conf;


import com.github.simplemocks.error_service.embedded.EnableErrorServiceEmbedded;
import com.github.simplemocks.error_service.mutable.api.source.ErrorLocalizationsJsonSource;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Objects;

/**
 * @author sibmaks
 * @since 0.0.10
 */
@ErrorLocalizationsJsonSource(
        systemCode = "ERROR_SERVICE",
        iso3Code = "eng",
        path = "classpath:/embedded/error/content/error/eng.json"
)
@ErrorLocalizationsJsonSource(
        systemCode = "ERROR_SERVICE",
        iso3Code = "rus",
        path = "classpath:/embedded/error/content/error/rus.json"
)
public class ErrorServiceEmbeddedCondition implements Condition {
    @Override
    public boolean matches(@Nonnull ConditionContext context,
                           @Nonnull AnnotatedTypeMetadata metadata) {
        var beanFactory = Objects.requireNonNull(context.getBeanFactory());
        return beanFactory
                .getBeanNamesForAnnotation(EnableErrorServiceEmbedded.class).length > 0;

    }
}
