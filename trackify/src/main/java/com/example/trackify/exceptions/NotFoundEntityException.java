package com.example.trackify.exceptions;

public class NotFoundEntityException extends RuntimeException {
    public <T> NotFoundEntityException(String nombreClase, T entidad, Throwable cause) {
        super("La entidad " + nombreClase + " no se ha podido encontrar: " + entidad, cause);
    }
}
