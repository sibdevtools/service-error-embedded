package com.github.simplemocks.error.embedded.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Embedded error service constants
 *
 * @author sibmaks
 * @since 0.0.10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    /**
     * Content service error type.
     */
    public static final String CONTENT_TYPE = "simple-mocks.errors";

    /**
     * Content service 'locale' attribute code
     */
    public static final String ATTRIBUTE_LOCALE = "locale";
    /**
     * Content service 'code' attribute code
     */
    public static final String ATTRIBUTE_CODE = "code";
}
