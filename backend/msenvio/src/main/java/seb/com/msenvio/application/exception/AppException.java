package seb.com.msenvio.application.exception;

import seb.com.msenvio.infrastructure.config.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AppException extends ApiException {
    public AppException(String mensaje) {
        super(mensaje, HttpStatus.NOT_IMPLEMENTED);
    }
}
