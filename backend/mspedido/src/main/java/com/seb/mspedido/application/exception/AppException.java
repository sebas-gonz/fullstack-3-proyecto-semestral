package com.seb.mspedido.application.exception;

import com.seb.mspedido.infrastructure.config.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AppException extends ApiException {
    public AppException(String mensaje) {
        super(mensaje, HttpStatus.NOT_IMPLEMENTED);
    }
}
