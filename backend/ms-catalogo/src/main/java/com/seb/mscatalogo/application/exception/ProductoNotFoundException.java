package com.seb.mscatalogo.application.exception;

import com.seb.mscatalogo.infrastructure.config.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ProductoNotFoundException extends ApiException {
    public ProductoNotFoundException(UUID id) {
        super("Producto con Id: " + id.toString() + " no encontrado", HttpStatus.NOT_FOUND);
    }
}
