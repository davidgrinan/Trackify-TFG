package com.example.trackify.exceptions;

public class NotFoundEntityException extends RuntimeException {
    public <T> NotFoundEntityException(String mensaje) {
        super(mensaje);
    }
}
