package com.github.simple_mocks.error.embedded.service;

import com.github.simple_mocks.content.api.condition.EqualsCondition;
import com.github.simple_mocks.content.api.rq.GetContentRq;
import com.github.simple_mocks.content.api.service.ContentService;
import com.github.simple_mocks.error.embedded.constants.Constants;
import com.github.simple_mocks.error_service.api.dto.LocalizedError;
import com.github.simple_mocks.error_service.api.rq.LocalizeErrorRq;
import com.github.simple_mocks.error_service.api.service.ErrorService;
import com.github.simple_mocks.error_service.exception.ServiceException;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Implementation of error service, use content service
 *
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
     * @param defaultTitle   default title
     * @param defaultMessage default message
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
    public LocalizedError localize(@Nonnull LocalizeErrorRq rq) {
        var localizationId = rq.errorLocalizationId();
        var localizationCode = localizationId.code();
        var sourceId = localizationId.sourceId();
        var userLocale = rq.userLocale();
        try {
            var contentRq = GetContentRq.<LocalizedError>builder()
                    .systemCode(sourceId.getSystemCode())
                    .type(Constants.CONTENT_TYPE)
                    .groupCode(sourceId.getKindCode())
                    .contentType(LocalizedError.class)
                    .conditions(
                            List.of(
                                    new EqualsCondition(Constants.ATTRIBUTE_CODE, localizationCode),
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

        return new LocalizedError(
                defaultTitle,
                defaultMessage,
                null
        );
    }
}
