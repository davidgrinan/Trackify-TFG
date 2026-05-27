package com.example.trackify.exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String mensaje) {
        super(mensaje);
    }

    public UnauthorizedException(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}