package com.vadymkykalo.lms.controller.api;

import com.vadymkykalo.lms.dto.ApiResponse;
import com.vadymkykalo.lms.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @Value("${app.env:prod}")
    private String appEnv;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("ResourceNotFoundException occurred: ", ex);

        String errorMessage = isDevMode() ? ex.getMessage() : "Resource not found";
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        log.error("Exception occurred: ", ex);

        String errorMessage = isDevMode() ? (ex.getMessage() != null ? ex.getMessage() : "No message available") :
                "An unexpected error occurred. Please contact support.";

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage));
    }

    private boolean isDevMode() {
        return "dev".equalsIgnoreCase(appEnv);
    }
}
