package com.github.sibdevtools.error.embedded.constants;

import com.github.sibdevtools.error.api.dto.ErrorSourceId;
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
    public static final String CONTENT_TYPE = "sibdevtools.errors";
    /**
     * Error source identifier
     */
    public static final ErrorSourceId ERROR_SOURCE_ID = new ErrorSourceId("error");

    /**
     * Content service 'locale' attribute code
     */
    public static final String ATTRIBUTE_LOCALE = "locale";
    /**
     * Content service 'code' attribute code
     */
    public static final String ATTRIBUTE_CODE = "code";
}
