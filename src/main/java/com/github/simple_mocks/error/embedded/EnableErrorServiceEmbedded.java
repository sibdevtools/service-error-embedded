package com.github.simple_mocks.error.embedded;

import com.github.simple_mocks.error.embedded.conf.ErrorServiceEmbeddedConfig;
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
