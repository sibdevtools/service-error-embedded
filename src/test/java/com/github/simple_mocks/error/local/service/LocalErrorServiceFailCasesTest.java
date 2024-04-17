package com.github.simple_mocks.error.local.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author sibmaks
 * @since 0.0.1
 */
class LocalErrorServiceFailCasesTest {

    @ParameterizedTest
    @MethodSource("setUpWhenLocaleIsNotPassedCases")
    void testSetUpWhenLocaleIsNotPassed(String filename) {
        var resource = mock(Resource.class);
        var resources = List.<Resource>of(resource);

        when(resource.getFilename())
                .thenReturn(filename);

        var objectMapper = new ObjectMapper();

        var localErrorService = new LocalErrorService(Locale.ENGLISH, resources, objectMapper);
        var exception = assertThrows(IllegalArgumentException.class, localErrorService::setUp);
        assertEquals("Locale code not passed", exception.getMessage());
    }

    public static Stream<Arguments> setUpWhenLocaleIsNotPassedCases() {
        return Stream.of(
                Arguments.of((String) null),
                Arguments.of("")
        );
    }

    @Test
    void testSetUpWhenDefaultLocaleHasNotLocalization() {
        var enResource = new ClassPathResource("samples/errors/en.json");
        var resources = List.<Resource>of(enResource);

        var objectMapper = new ObjectMapper();

        var localErrorService = new LocalErrorService(Locale.FRANCE, resources, objectMapper);
        var exception = assertThrows(IllegalArgumentException.class, localErrorService::setUp);
        assertEquals("Default locale hasn't localized", exception.getMessage());
    }

}