package com.pricetracker.api.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorMessageResponse {
    private String timestamp;
    private HttpStatus status;
    private String message;
    private String developerMessage;
}
