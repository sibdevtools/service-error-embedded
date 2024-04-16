package com.github.simple_mocks.error.local;

import com.github.simple_mocks.error.local.conf.LocalErrorServiceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enabler for local error service implementation
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(LocalErrorServiceConfig.class)
public @interface EnableLocalErrorService {
    /**
     * Resource path of error descriptions.<br/>
     * Example: {@code "classpath:/config/mocks/errors/"}
     *
     * @return resource path
     */
    String value();

    /**
     * Default locale, in case if requested isn't presented
     *
     * @return default locale
     */
    String defaultLocale() default "en";
}
