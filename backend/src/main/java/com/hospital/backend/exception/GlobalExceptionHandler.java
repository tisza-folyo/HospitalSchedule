package com.hospital.backend.exception;

import com.hospital.backend.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), null));
    }

    @ExceptionHandler({CollisionException.class, AlreadyExistsException.class})
    public ResponseEntity<ApiResponse> handleConflict(Exception e) {
        return ResponseEntity.status(CONFLICT)
                .body(new ApiResponse(e.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneralError(Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(e.getMessage(), null));
    }
}
