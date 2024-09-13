package com.github.simplemocks.error_service.embedded.source.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Error localization dto
 *
 * @author sibmaks
 * @since 0.0.10
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonErrorDto {
    private String title;
    private String message;
    private String systemMessage;
}
