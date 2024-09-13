package com.github.simplemocks.error_service.embedded.source;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.simplemocks.error_service.api.dto.ErrorSourceId;
import com.github.simplemocks.error_service.api.dto.LocalizedError;
import com.github.simplemocks.error_service.embedded.conf.ErrorServiceEmbeddedCondition;
import com.github.simplemocks.error_service.embedded.exception.ErrorLocalizationsLoadingException;
import com.github.simplemocks.error_service.embedded.source.dto.JsonErrorsDto;
import com.github.simplemocks.error_service.mutable.api.dto.ErrorLocalizationKey;
import com.github.simplemocks.error_service.mutable.api.rq.AddLocalizationsRq;
import com.github.simplemocks.error_service.mutable.api.service.MutableErrorService;
import com.github.simplemocks.error_service.mutable.api.source.ErrorLocalizationsJsonSource;
import com.github.simplemocks.error_service.mutable.api.source.ErrorLocalizationsJsonSources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.3
 */
@Slf4j
@Component
@Conditional(ErrorServiceEmbeddedCondition.class)
public class ErrorLocalizationsJsonLoader {
    private final MutableErrorService mutableErrorService;
    private final ObjectMapper objectMapper;
    private final ApplicationContext context;
    private final ResourceLoader resourceLoader;

    public ErrorLocalizationsJsonLoader(MutableErrorService mutableErrorService,
                                        @Qualifier("errorServiceObjectMapper")
                                  ObjectMapper objectMapper,
                                        ApplicationContext context,
                                        ResourceLoader resourceLoader) {
        this.mutableErrorService = mutableErrorService;
        this.objectMapper = objectMapper;
        this.context = context;
        this.resourceLoader = resourceLoader;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        var beans = context.getBeanNamesForAnnotation(ErrorLocalizationsJsonSources.class);
        for (var bean : beans) {
            var sources = context.findAllAnnotationsOnBean(bean, ErrorLocalizationsJsonSource.class, true);
            for (var source : sources) {
                loadLocalizations(source);
            }
        }
    }

    private void loadLocalizations(ErrorLocalizationsJsonSource source) {
        var resource = resourceLoader.getResource(source.path());
        var errorSourceId = new ErrorSourceId(source.systemCode(), source.kindCode());
        var locale = Locale.of(source.iso3Code());
        JsonErrorsDto jsonErrorsDto;
        try {
            jsonErrorsDto = objectMapper.readValue(resource.getInputStream(), JsonErrorsDto.class);
        } catch (IOException e) {
            throw new ErrorLocalizationsLoadingException("Source read exception", e);
        }
        var errorLocalizations = jsonErrorsDto
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        it -> new ErrorLocalizationKey(it.getKey(), locale),
                        it -> {
                            var jsonErrorDto = it.getValue();
                            return new LocalizedError(
                                    jsonErrorDto.getTitle(),
                                    jsonErrorDto.getMessage()
                            );
                        }
                ));
        mutableErrorService.addLocalizations(
                new AddLocalizationsRq(errorSourceId, errorLocalizations)
        );
    }
}
