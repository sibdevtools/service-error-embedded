package com.github.sibdevtools.error.embedded.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.sibdevtools.error.mutable.api.source.ErrorLocalizationsJsonSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Embedded error service configuration
 *
 * @author sibmaks
 * @since 0.0.1
 */
@ErrorLocalizationsJsonSource(
        systemCode = "error",
        iso3Code = "eng",
        path = "classpath:/embedded/error/content/error/eng.json"
)
@ErrorLocalizationsJsonSource(
        systemCode = "error",
        iso3Code = "rus",
        path = "classpath:/embedded/error/content/error/rus.json"
)
@Configuration
@PropertySource("classpath:/embedded/error/application.properties")
@ConditionalOnProperty(name = "service.error.mode", havingValue = "EMBEDDED")
public class ErrorServiceEmbeddedConfig {

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

}
