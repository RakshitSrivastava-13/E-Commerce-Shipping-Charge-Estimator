package com.jumbotail.shipping.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Standard error response returned by the global exception handler.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}
