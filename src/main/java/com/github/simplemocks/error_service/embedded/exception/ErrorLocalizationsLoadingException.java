package com.github.simplemocks.error_service.embedded.exception;

import com.github.simplemocks.error_service.exception.ServiceException;
import com.github.simplemocks.error_service.embedded.constants.Constants;
import jakarta.annotation.Nonnull;

/**
 * @author sibmaks
 * @since 0.0.10
 */
public class ErrorLocalizationsLoadingException extends ServiceException {
    public ErrorLocalizationsLoadingException(@Nonnull String systemMessage, @Nonnull Throwable cause) {
        super(Constants.ERROR_SOURCE_ID, "ERROR_LOCALIZATIONS_LOADING_EXCEPTION", systemMessage, cause);
    }
}
