package com.municipalidad.catastro.handler;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String type,
        String title,
        Integer status,
        String detail,
        String instance,
        LocalDateTime timestamp
) {
    public ErrorResponse(String detail) {
        this("about:blank", "Error", 400, detail, null, LocalDateTime.now());
    }

    public ErrorResponse(String title, Integer status, String detail) {
        this("about:blank", title, status, detail, null, LocalDateTime.now());
    }
}
