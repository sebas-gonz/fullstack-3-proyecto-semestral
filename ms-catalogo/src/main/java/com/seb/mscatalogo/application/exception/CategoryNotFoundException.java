package com.seb.mscatalogo.application.exception;

import com.seb.mscatalogo.infrastructure.config.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class CategoryNotFoundException extends ApiException {
    public CategoryNotFoundException(UUID id) {
        super("Categoria con id: " + id.toString() + " no encontrado", HttpStatus.NOT_FOUND);
        }




}
