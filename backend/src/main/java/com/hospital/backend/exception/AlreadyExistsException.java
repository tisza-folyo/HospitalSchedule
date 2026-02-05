package com.hospital.backend.exception;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String message) {
        super(message + " already exists!");
    }
}
