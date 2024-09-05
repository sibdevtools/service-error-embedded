package com.github.simple_mocks.error.embedded.service;

import com.github.simple_mocks.content.api.condition.EqualsCondition;
import com.github.simple_mocks.content.api.rq.GetContentRq;
import com.github.simple_mocks.content.api.service.ContentService;
import com.github.simple_mocks.error.embedded.constants.Constants;
import com.github.simple_mocks.error_service.api.ErrorLocalization;
import com.github.simple_mocks.error_service.api.ErrorSource;
import com.github.simple_mocks.error_service.api.service.ErrorService;
import com.github.simple_mocks.error_service.exception.ServiceException;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
public class ErrorServiceEmbedded implements ErrorService {

    private final String defaultTitle;
    private final String defaultMessage;
    private final ContentService contentService;

    /**
     * Constructor embedded error service
     *
     * @param contentService content service
     */
    public ErrorServiceEmbedded(String defaultTitle,
                                String defaultMessage,
                                ContentService contentService) {
        this.defaultTitle = defaultTitle;
        this.defaultMessage = defaultMessage;
        this.contentService = contentService;
    }

    @Nonnull
    @Override
    public ErrorLocalization localize(@Nonnull ErrorSource errorSource,
                                      @Nonnull String errorCode,
                                      @Nonnull Locale userLocale) {
        try {
            var contentRq = GetContentRq.<ErrorLocalization>builder()
                    .systemCode(errorSource.getSystemCode())
                    .type(Constants.ERROR_TYPE)
                    .groupCode(errorSource.getKindCode())
                    .contentType(ErrorLocalization.class)
                    .conditions(
                            List.of(
                                    new EqualsCondition(Constants.ATTRIBUTE_CODE, errorCode),
                                    new EqualsCondition(Constants.ATTRIBUTE_LOCALE, userLocale.getISO3Language())
                            )
                    )
                    .build();

            var rs = contentService.getContent(contentRq);

            var contents = rs.contents();

            for (var entry : contents.entrySet()) {
                var contentHolder = entry.getValue();
                if (contentHolder != null) {
                    return contentHolder.getContent();
                }
            }
        } catch (ServiceException e) {
            log.error("Failed to localize error: ", e);
        }

        return new ErrorLocalization(
                defaultTitle,
                defaultMessage,
                null
        );
    }
}
