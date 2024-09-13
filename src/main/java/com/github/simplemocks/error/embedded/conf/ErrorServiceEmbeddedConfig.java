package com.github.simplemocks.error.embedded.conf;

import com.github.simplemocks.content.api.service.ContentService;
import com.github.simplemocks.content.mutable.api.service.MutableContentService;
import com.github.simplemocks.error.embedded.EnableErrorServiceEmbedded;
import com.github.simplemocks.error.embedded.service.ErrorServiceEmbedded;
import com.github.simplemocks.error.embedded.service.MutableErrorServiceEmbedded;
import com.github.simplemocks.error_service.api.service.ErrorService;
import com.github.simplemocks.error_service.mutable.api.service.MutableErrorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Embedded error service configuration
 *
 * @author sibmaks
 * @since 0.0.1
 */
public class ErrorServiceEmbeddedConfig implements ImportAware {
    private String defaultTitle;
    private String defaultMessage;

    /**
     * Construct embedded error service bean
     *
     * @param contentService content service
     * @return instance of embedded error service
     */
    @Bean
    public ErrorService errorServiceEmbedded(ContentService contentService) {
        return new ErrorServiceEmbedded(defaultTitle, defaultMessage, contentService);
    }

    /**
     * Construct embedded mutable error service bean
     *
     * @param mutableContentService mutable content service
     * @return instance of embedded mutable error service
     */
    @Bean
    public MutableErrorService mutableErrorServiceEmbedded(MutableContentService mutableContentService) {
        return new MutableErrorServiceEmbedded(mutableContentService);
    }

    @Override
    public void setImportMetadata(AnnotationMetadata metadata) {
        var attributes = metadata.getAnnotationAttributes(EnableErrorServiceEmbedded.class.getName());
        if (attributes == null) {
            throw new IllegalStateException("No attributes in annotation @EnableErrorServiceEmbedded");
        }
        this.defaultTitle = (String) attributes.get("defaultTitle");
        if (StringUtils.isBlank(defaultTitle)) {
            throw new IllegalArgumentException("defaultTitle of @EnableErrorServiceEmbedded should not be null or blank");
        }
        this.defaultMessage = (String) attributes.get("defaultMessage");
        if (StringUtils.isBlank(defaultMessage)) {
            throw new IllegalArgumentException("defaultMessage of @EnableErrorServiceEmbedded should not be null or blank");
        }
    }

}
