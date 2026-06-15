package com.example.OhBike.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " con ID " + id + " no fue encontrado.");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}