package com.seb.msusuario.application.exception;

import com.seb.msusuario.infrastructure.config.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(String id){
        super("Usuario con id " + id + " no encontrado",HttpStatus.NOT_FOUND);
    }
}
