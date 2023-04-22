package com.github.sibmaks.error.local.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.error_service.ErrorService;
import com.github.sibmaks.error_service.api.ErrorDescription;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public class LocalErrorService implements ErrorService {
    private final Resource resource;
    private final ObjectMapper objectMapper;
    private final Map<String, Map<String, ErrorDescription>> systemErrorDescriptions;

    public LocalErrorService(Resource resource, ObjectMapper objectMapper) {
        this.resource = resource;
        this.objectMapper = objectMapper;
        this.systemErrorDescriptions = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void setUp() throws IOException {
        systemErrorDescriptions.clear();
        var contentArray = resource.getContentAsByteArray();
        var errorDescriptions = objectMapper.readValue(contentArray, ErrorDescription[].class);
        for (var errorDescription : errorDescriptions) {
            var systemCode = errorDescription.getSystemCode();
            var subsystemErrors = systemErrorDescriptions.computeIfAbsent(systemCode,
                    it -> new HashMap<>());
            var code = errorDescription.getCode();
            subsystemErrors.put(code, errorDescription);
        }
    }

    @Override
    public ErrorDescription getDescription(String systemCode, String code) {
        var systemErrors = systemErrorDescriptions.get(systemCode);
        if(systemErrors == null) {
            throw new IllegalArgumentException("Error descriptions for system %s not found.".formatted(systemCode));
        }
        return systemErrors.get(code);
    }
}
