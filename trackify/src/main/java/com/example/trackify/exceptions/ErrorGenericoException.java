package com.example.trackify.exceptions;

public class ErrorGenericoException extends RuntimeException {
    public <T> ErrorGenericoException(String error, Throwable cause) {
        super(error, cause);
    }
}
