package com.github.simplemocks.error_service.embedded;

import com.github.simplemocks.error_service.embedded.conf.ErrorServiceEmbeddedConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enabler for embedded error service implementation
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ErrorServiceEmbeddedConfig.class)
public @interface EnableErrorServiceEmbedded {
    /**
     * Default title, in case if localization isn't presented
     *
     * @return default title
     */
    String defaultTitle() default "Unexpected error";

    /**
     * Default message, in case if localization isn't presented
     *
     * @return default message
     */
    String defaultMessage() default "Unexpected error";
}
