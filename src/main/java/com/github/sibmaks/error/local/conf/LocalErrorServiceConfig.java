package com.github.sibmaks.error.local.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.error.local.EnableLocalErrorService;
import com.github.sibmaks.error.local.service.LocalErrorService;
import com.github.sibmaks.error_service.ErrorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public class LocalErrorServiceConfig implements ImportAware {
    private String configLocation;

    @Bean
    public ErrorService errorService(ObjectMapper objectMapper,
                                     DefaultResourceLoader resourceLoader) {
        var errorsConfig = resourceLoader.getResource(configLocation);
        return new LocalErrorService(errorsConfig, objectMapper);
    }

    @Override
    public void setImportMetadata(AnnotationMetadata metadata) {
        var attributes = metadata.getAnnotationAttributes(EnableLocalErrorService.class.getName());
        if(attributes == null) {
            throw new IllegalStateException("No attributes in annotation @EnableLocalErrorService");
        }
        var resourcePath = (String) attributes.get("value");
        if(resourcePath == null || resourcePath.isBlank()) {
            throw new IllegalArgumentException("Value of @EnableLocalErrorService should not be null or blank");
        }
        this.configLocation = resourcePath;
    }

}
