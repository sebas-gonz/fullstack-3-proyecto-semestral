package com.seb.msusuario.application.exception;

import com.seb.msusuario.infrastructure.config.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UserEmailExistException extends ApiException {
    public UserEmailExistException(String email) {
        super("El correo electronico: " + email +  " ya esta registrado.", HttpStatus.CONFLICT);
    }
}
