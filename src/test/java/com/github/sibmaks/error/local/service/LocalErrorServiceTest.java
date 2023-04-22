package com.github.sibmaks.error.local.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author sibmaks
 * @since 2023-04-22
 */
class LocalErrorServiceTest {
    private static LocalErrorService localErrorService;

    @BeforeAll
    static void setUp() throws IOException {
        var resource = new ClassPathResource("samples/errors.json");
        var objectMapper = new ObjectMapper();

        localErrorService = new LocalErrorService(resource, objectMapper);
        localErrorService.setUp();
    }

    @Test
    void testGetExistedDescription() {
        var description = localErrorService.getDescription("ERROR_SYSTEM_CODE", "ERROR_CODE");
        assertEquals("ERROR_SYSTEM_CODE", description.getSystemCode());
        assertEquals("ERROR_CODE", description.getCode());
        assertEquals("ERROR_TITLE", description.getTitle());
        assertEquals("ERROR_MESSAGE", description.getMessage());
    }

    @Test
    void testGetWhenSystemNotRegistered() {
        var systemCode = UUID.randomUUID().toString();
        var code = UUID.randomUUID().toString();

        var exception = assertThrows(IllegalArgumentException.class,
                () -> localErrorService.getDescription(systemCode, code));
        assertEquals("Error descriptions for system %s not found.".formatted(systemCode), exception.getMessage());
    }

}