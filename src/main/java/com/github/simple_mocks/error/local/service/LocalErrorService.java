package com.github.simple_mocks.error.local.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.simple_mocks.error_service.ErrorService;
import com.github.simple_mocks.error_service.api.ErrorDescription;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class LocalErrorService implements ErrorService {
    private final Locale defaultLocale;
    private final List<Resource> errorsConfigs;
    private final ObjectMapper objectMapper;
    private final Map<Locale, Map<String, Map<String, ErrorDescription>>> localizedSystemErrorDescriptions;

    /**
     * Constructor local error service
     *
     * @param defaultLocale default error localization
     * @param errorsConfigs error configurations
     * @param objectMapper  object mapper
     */
    public LocalErrorService(Locale defaultLocale,
                             List<Resource> errorsConfigs,
                             ObjectMapper objectMapper) {
        this.defaultLocale = defaultLocale;
        this.errorsConfigs = errorsConfigs;
        this.objectMapper = objectMapper;
        this.localizedSystemErrorDescriptions = new ConcurrentHashMap<>();
    }

    /**
     * Setup local error service, reload localizations from config files
     *
     * @throws IOException can be caused when config file loaded
     */
    @PostConstruct
    public void setUp() throws IOException {
        localizedSystemErrorDescriptions.clear();
        for (var errorsConfig : errorsConfigs) {
            var localeCode = errorsConfig.getFilename();
            if (StringUtils.isBlank(localeCode)) {
                throw new IllegalArgumentException("Locale code not passed");
            }
            localeCode = localeCode.substring(0, localeCode.length() - ".json".length());
            var locale = Locale.forLanguageTag(localeCode);
            var systemErrorDescriptions = localizedSystemErrorDescriptions.computeIfAbsent(locale, it -> new HashMap<>());

            var contentArray = errorsConfig.getContentAsByteArray();
            var errorDescriptions = objectMapper.readValue(contentArray, ErrorDescription[].class);
            for (var errorDescription : errorDescriptions) {
                var systemCode = errorDescription.getSystemCode();
                var subsystemErrors = systemErrorDescriptions.computeIfAbsent(systemCode,
                        it -> new HashMap<>());
                var code = errorDescription.getCode();
                subsystemErrors.put(code, errorDescription);
            }
        }
        if (!localizedSystemErrorDescriptions.containsKey(defaultLocale)) {
            throw new IllegalArgumentException("Default locale hasn't localized");
        }
    }

    @Nonnull
    @Override
    public ErrorDescription getDescription(@Nonnull String systemCode,
                                           @Nonnull String code,
                                           @Nonnull Locale locale) {
        var systemErrorDescriptions = localizedSystemErrorDescriptions.computeIfAbsent(
                locale,
                it -> localizedSystemErrorDescriptions.get(defaultLocale)
        );
        var systemErrors = systemErrorDescriptions.get(systemCode);
        if (systemErrors == null) {
            throw new IllegalArgumentException("Error descriptions for system %s not found.".formatted(systemCode));
        }
        return systemErrors.get(code);
    }
}
