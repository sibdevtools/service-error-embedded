package com.github.simplemocks.error_service.embedded.source.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Json file dto
 *
 * @author sibmaks
 * @since 0.0.10
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonErrorsDto {
    private Map<String, JsonErrorDto> errors;
}
