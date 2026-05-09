package com.seb.mscatalogo.infrastructure.config.exception;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
    private final HttpStatus status;

    public ApiException(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
