package com.seb.msinventario.application.exception;

import com.seb.msinventario.infrastructure.config.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class InventoryNotFoundException extends ApiException {
    public InventoryNotFoundException(UUID Id) {
        super("Inventario con id: " + Id.toString() + " no encontrado", HttpStatus.NOT_FOUND);
    }
}
