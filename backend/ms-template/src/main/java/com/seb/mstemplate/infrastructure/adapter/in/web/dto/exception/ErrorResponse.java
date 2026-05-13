package com.seb.mstemplate.infrastructure.adapter.in.web.dto.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String mensaje,
        int statusCode,
        LocalDateTime registro,
        List<String> errores) {
    public ErrorResponse(String mensaje, int statusCode) {
        this(mensaje, statusCode,LocalDateTime.now(), null);
    }
}
