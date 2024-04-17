package com.github.simple_mocks.error.local.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * @author sibmaks
 * @since 0.0.1
 */
class LocalErrorServiceTest {
    private LocalErrorService localErrorService;

    @BeforeEach
    void setUp() throws IOException {
        var enResource = new ClassPathResource("samples/errors/en.json");
        var ruRUResource = new ClassPathResource("samples/errors/ru_RU.json");
        var resources = List.<Resource>of(enResource, ruRUResource);

        var objectMapper = new ObjectMapper();

        localErrorService = new LocalErrorService(Locale.ENGLISH, resources, objectMapper);
        localErrorService.setUp();
    }

    @ParameterizedTest
    @MethodSource("getExistedDescriptionCases")
    void testGetExistedDescription(Locale locale, String title, String message) {
        var description = localErrorService.getDescription("ERROR_SYSTEM_CODE", "ERROR_CODE", locale);
        assertEquals("ERROR_SYSTEM_CODE", description.getSystemCode());
        assertEquals("ERROR_CODE", description.getCode());
        assertEquals(title, description.getTitle());
        assertEquals(message, description.getMessage());
    }

    public static Stream<Arguments> getExistedDescriptionCases() {
        return Stream.of(
                Arguments.of(Locale.ENGLISH, "ENG-title", "ENG-message"),
                Arguments.of(Locale.UK, "ENG-title", "ENG-message"),
                Arguments.of(Locale.FRANCE, "ENG-title", "ENG-message"),
                Arguments.of(Locale.forLanguageTag("ru_RU"), "ru_RU-заголовок", "ru_RU-сообщение")
        );
    }

    @Test
    void testGetWhenSystemNotRegistered() {
        var systemCode = UUID.randomUUID().toString();
        var code = UUID.randomUUID().toString();
        var locale = mock(Locale.class);

        var exception = assertThrows(IllegalArgumentException.class,
                () -> localErrorService.getDescription(systemCode, code, locale));
        assertEquals("Error descriptions for system %s not found.".formatted(systemCode), exception.getMessage());
    }

}