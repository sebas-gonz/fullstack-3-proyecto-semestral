package com.seb.msusuario.application.exception;

import com.seb.msusuario.infrastructure.config.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class AddressNotFoundException extends ApiException {
    public AddressNotFoundException(UUID id) {
        super("Direccion con id: " + id + " no encontrado", HttpStatus.NOT_FOUND);
    }
}
