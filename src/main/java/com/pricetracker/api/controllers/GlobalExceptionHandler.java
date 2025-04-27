package com.pricetracker.api.controllers;

import com.pricetracker.api.exceptions.PriceTrackerException;
import com.pricetracker.api.exceptions.PriceTrackerNotFoundException;
import com.pricetracker.api.models.ErrorMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PriceTrackerNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handlePriceTrackerNotFound(PriceTrackerNotFoundException ex) {
        log.error("Error occurred while processing price tracker request", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorMessageResponse.builder()
                                .timestamp(LocalDateTime.now().toString())
                                .status(HttpStatus.NOT_FOUND)
                                .message("The requested product could not be found.")
                                .build()
                );
    }

    @ExceptionHandler(PriceTrackerException.class)
    public ResponseEntity<ErrorMessageResponse> handlePriceTrackerException(PriceTrackerException ex) {
        log.error("Error occurred while processing price tracker request", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorMessageResponse.builder()
                                .timestamp(LocalDateTime.now().toString())
                                .status(HttpStatus.BAD_REQUEST)
                                .message("There was a problem processing your request. Please verify your input.")
                                .build()
                );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleNoResourceFound(NoResourceFoundException ex) {
        log.error("Error occurred while processing price tracker request", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorMessageResponse.builder()
                                .timestamp(LocalDateTime.now().toString())
                                .status(HttpStatus.NOT_FOUND)
                                .message("The requested URL was not found. Please check the path and try again.")
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Error occurred while processing price tracker request", ex);
        StringBuilder errors = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorMessageResponse.builder()
                                .timestamp(LocalDateTime.now().toString())
                                .status(HttpStatus.BAD_REQUEST)
                                .message("Validation failed for the given parameters.")
                                .developerMessage(errors.toString())
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleGeneralException(Exception ex) {
        log.error("Error occurred while processing price tracker request", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorMessageResponse.builder()
                                .timestamp(LocalDateTime.now().toString())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .message("An unexpected error occurred. Please try again later.")
                                .build()
                );
    }
}
