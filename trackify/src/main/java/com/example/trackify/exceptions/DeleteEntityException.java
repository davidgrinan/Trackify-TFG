package com.example.trackify.exceptions;

public class DeleteEntityException extends RuntimeException {
    public <T> DeleteEntityException(String nombreClase, T entidad, Throwable cause) {
        super("La entidad " + nombreClase + " no se ha podido eliminar: " + entidad, cause);
    }
}
