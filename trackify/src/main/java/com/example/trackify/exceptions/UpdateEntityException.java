package com.example.trackify.exceptions;

public class UpdateEntityException extends RuntimeException {
    public <T> UpdateEntityException(String nombreClase, T entidad, Throwable cause) {
        super("La entidad " + nombreClase + " no se ha podido actualizar: " + entidad, cause);
    }
}
