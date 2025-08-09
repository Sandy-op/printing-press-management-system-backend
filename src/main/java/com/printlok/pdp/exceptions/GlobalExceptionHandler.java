package com.printlok.pdp.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.printlok.pdp.dto.ResponseStructure;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ResponseStructure<String>> handleDuplicateEmail(DuplicateEmailException ex) {
        ResponseStructure<String> response = new ResponseStructure<>();
        response.setMessage("User creation failed");
        response.setData(ex.getMessage());
        response.setStatusCode(HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DuplicatePhoneException.class)
    public ResponseEntity<ResponseStructure<String>> handleDuplicatePhone(DuplicatePhoneException ex) {
        ResponseStructure<String> response = new ResponseStructure<>();
        response.setMessage("User creation failed");
        response.setData(ex.getMessage());
        response.setStatusCode(HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseStructure<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        ResponseStructure<Map<String, String>> response = new ResponseStructure<>();
        response.setMessage("Validation failed");
        response.setData(errors);
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStructure<String>> handleOtherExceptions(Exception ex) {
        ResponseStructure<String> response = new ResponseStructure<>();
        response.setMessage("Unexpected error occurred");
        response.setData(ex.getMessage());
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ResponseStructure<String>> handleInvalidRole(InvalidRoleException ex) {
        ResponseStructure<String> structure = new ResponseStructure<>();
        structure.setMessage("Role assignment failed");
        structure.setData(ex.getMessage());
        structure.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(structure);
    }

}