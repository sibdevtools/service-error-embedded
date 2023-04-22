package com.github.sibmaks.error.local;

import com.github.sibmaks.error.local.conf.LocalErrorServiceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enabler for local error service implementation
 *
 * @author sibmaks
 * @since 2023-04-11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(LocalErrorServiceConfig.class)
public @interface EnableLocalErrorService {
    /**
     * Resource path of error descriptions.<br/>
     * Example: "classpath:/config/mocks/errors.json"
     *
     * @return resource path
     */
    String value();
}
