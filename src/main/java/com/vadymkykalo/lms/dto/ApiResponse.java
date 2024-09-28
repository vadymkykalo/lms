package com.vadymkykalo.lms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    public enum Result {
        success, error
    }
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    private int code;
    private Result result;
    private String error;
    private T data;

    public static <T> ApiResponse<T> success(T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .code(status.value())
                .result(Result.success)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.OK.value())
                .result(Result.success)
                .data(data)
                .build();
    }

    public static ApiResponse<?> error(HttpStatus status, String errorMessage) {
        return ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(status.value())
                .result(Result.error)
                .error(errorMessage)
                .build();
    }
}