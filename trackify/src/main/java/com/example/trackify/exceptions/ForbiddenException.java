package com.example.trackify.exceptions;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String mensaje) {
        super(mensaje);
    }

    public ForbiddenException(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}