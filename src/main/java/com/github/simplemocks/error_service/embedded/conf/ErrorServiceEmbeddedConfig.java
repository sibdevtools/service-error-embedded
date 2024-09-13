package com.github.simplemocks.error_service.embedded.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.simplemocks.content.api.service.ContentService;
import com.github.simplemocks.error_service.api.service.ErrorService;
import com.github.simplemocks.error_service.embedded.EnableErrorServiceEmbedded;
import com.github.simplemocks.error_service.embedded.service.ErrorServiceEmbedded;
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

    @Bean("errorServiceObjectMapper")
    public ObjectMapper errorServiceObjectMapper() {
        return JsonMapper.builder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();
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
