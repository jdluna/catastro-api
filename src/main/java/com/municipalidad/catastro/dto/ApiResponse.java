package com.municipalidad.catastro.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        String error,
        LocalDateTime timestamp,
        Integer statusCode
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                true,
                "Operación exitosa",
                data,
                null,
                LocalDateTime.now(),
                200
        );
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                message,
                data,
                null,
                LocalDateTime.now(),
                200
        );
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(
                true,
                message,
                data,
                null,
                LocalDateTime.now(),
                201
        );
    }

    public static <T> ApiResponse<T> error(String error) {
        return new ApiResponse<>(
                false,
                null,
                null,
                error,
                LocalDateTime.now(),
                400
        );
    }

    public static <T> ApiResponse<T> error(String error, Integer statusCode) {
        return new ApiResponse<>(
                false,
                null,
                null,
                error,
                LocalDateTime.now(),
                statusCode
        );
    }

    public static <T> ApiResponse<T> notFound(String error) {
        return new ApiResponse<>(
                false,
                null,
                null,
                error,
                LocalDateTime.now(),
                404
        );
    }
}
