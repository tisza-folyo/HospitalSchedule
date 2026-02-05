package com.hospital.backend.exception;

public class CollisionException extends RuntimeException{
    public CollisionException(String message) {
        super(message + " collision!");
    }
}
