package com.seb.msinventario.application.exception;

import com.seb.msinventario.infrastructure.config.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AppException extends ApiException {
    public AppException(String mensaje) {
        super(mensaje, HttpStatus.NOT_IMPLEMENTED);
    }
}
