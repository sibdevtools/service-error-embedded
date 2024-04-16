package com.github.simple_mocks.error.local.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.simple_mocks.error.local.EnableLocalErrorService;
import com.github.simple_mocks.error.local.feature.WhiteBox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author sibmaks
 * @since 0.0.1
 */
class LocalErrorServiceConfigTest {

    @Test
    void testSetImportMetadataWhenNoAnnotationAttributes() {
        var metadata = mock(AnnotationMetadata.class);
        when(metadata.getAnnotationAttributes(EnableLocalErrorService.class.getName()))
                .thenReturn(null);

        var config = new LocalErrorServiceConfig();

        var exception = assertThrows(IllegalStateException.class, () -> config.setImportMetadata(metadata));

        assertEquals("No attributes in annotation @EnableLocalErrorService", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testSetImportMetadataWhenNoValue(boolean contains) {
        Map<String, Object> attributes = new HashMap<>();
        if (contains) {
            attributes.put("value", "");
        }

        var metadata = mock(AnnotationMetadata.class);
        when(metadata.getAnnotationAttributes(EnableLocalErrorService.class.getName()))
                .thenReturn(attributes);

        var config = new LocalErrorServiceConfig();

        var exception = assertThrows(IllegalArgumentException.class,
                () -> config.setImportMetadata(metadata));

        assertEquals("Value of @EnableLocalErrorService should not be null or blank", exception.getMessage());
    }

    @Test
    void testSetImportMetadataWhenNoDefaultLocale() {
        var path = UUID.randomUUID().toString();
        Map<String, Object> attributes = Map.of("value", path);

        var metadata = mock(AnnotationMetadata.class);
        when(metadata.getAnnotationAttributes(EnableLocalErrorService.class.getName()))
                .thenReturn(attributes);

        var config = new LocalErrorServiceConfig();

        var exception = assertThrows(IllegalArgumentException.class,
                () -> config.setImportMetadata(metadata));

        assertEquals("Default locale of @EnableLocalErrorService should not be null or blank", exception.getMessage());
    }

    @Test
    void testSetImportMetadata() throws NoSuchFieldException, IllegalAccessException {
        var path = UUID.randomUUID().toString();
        var defaultLocale = UUID.randomUUID().toString();
        Map<String, Object> attributes = Map.of(
                "value", path,
                "defaultLocale", defaultLocale
        );

        var metadata = mock(AnnotationMetadata.class);
        when(metadata.getAnnotationAttributes(EnableLocalErrorService.class.getName()))
                .thenReturn(attributes);

        var config = new LocalErrorServiceConfig();

        config.setImportMetadata(metadata);

        var exceptedPath = path + "/*.json";
        var configLocation = WhiteBox.getField(config, "configLocation");
        assertEquals(exceptedPath, configLocation);
    }

    @Test
    void testErrorService() throws NoSuchFieldException, IllegalAccessException, IOException {
        var configLocation = UUID.randomUUID().toString();

        var config = new LocalErrorServiceConfig();
        WhiteBox.setField(config, "configLocation", configLocation);

        var objectMapper = mock(ObjectMapper.class);
        var resourceLoader = mock(DefaultResourceLoader.class);

        var errorsConfig = mock(Resource.class);
        when(resourceLoader.getResource(configLocation))
                .thenReturn(errorsConfig);

        var errorService = config.localErrorService(objectMapper, resourceLoader);
        assertNotNull(errorService);
    }
}