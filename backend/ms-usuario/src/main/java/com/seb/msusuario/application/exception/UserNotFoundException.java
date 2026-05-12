package com.seb.msusuario.application.exception;

import com.seb.msusuario.infrastructure.config.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(UUID id){
        super("Usuario con id " + id + " no encontrado",HttpStatus.NOT_FOUND);
    }
    public UserNotFoundException(String id){
        super("Usuario con id " + id + " no encontrado",HttpStatus.NOT_FOUND);
    }
}
