package com.github.sibdevtools.error.embedded.service;

import com.github.sibdevtools.content.api.condition.EqualsCondition;
import com.github.sibdevtools.content.api.rq.GetContentRq;
import com.github.sibdevtools.content.api.service.ContentService;
import com.github.sibdevtools.error.embedded.conf.ErrorServiceEmbeddedProperties;
import com.github.sibdevtools.error.embedded.constants.Constants;
import com.github.sibdevtools.error.api.dto.LocalizedError;
import com.github.sibdevtools.error.api.rq.LocalizeErrorRq;
import com.github.sibdevtools.error.api.rs.LocalizeErrorRs;
import com.github.sibdevtools.error.api.service.ErrorService;
import com.github.sibdevtools.error.exception.ServiceException;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of error service, use content service
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "service.error.mode", havingValue = "EMBEDDED")
public class ErrorServiceEmbedded implements ErrorService {

    private final ErrorServiceEmbeddedProperties properties;
    private final ContentService contentService;

    /**
     * Constructor embedded error service
     *
     * @param properties     embedded error service properties
     * @param contentService content service
     */
    @Autowired
    public ErrorServiceEmbedded(ErrorServiceEmbeddedProperties properties,
                                ContentService contentService) {
        this.properties = properties;
        this.contentService = contentService;
    }

    @Nonnull
    @Override
    public LocalizeErrorRs localize(@Nonnull LocalizeErrorRq rq) {
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

            var contents = rs.getBody();

            for (var entry : contents.entrySet()) {
                var contentHolder = entry.getValue();
                if (contentHolder != null) {
                    var localizedError = contentHolder.getContent();
                    return new LocalizeErrorRs(localizedError);
                }
            }
        } catch (ServiceException e) {
            log.error("Failed to localize error: ", e);
        }

        var localizedError = new LocalizedError(
                properties.getDefaultTitle(),
                properties.getDefaultMessage()
        );
        return new LocalizeErrorRs(localizedError);
    }
}
