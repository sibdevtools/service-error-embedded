package com.github.simplemocks.error_service.embedded.constants;

import com.github.simplemocks.error_service.api.dto.ErrorSourceId;
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
     * Error source identifier
     */
    public static final ErrorSourceId ERROR_SOURCE_ID = new ErrorSourceId("ERROR_SERVICE");

    /**
     * Content service 'locale' attribute code
     */
    public static final String ATTRIBUTE_LOCALE = "locale";
    /**
     * Content service 'code' attribute code
     */
    public static final String ATTRIBUTE_CODE = "code";
}
