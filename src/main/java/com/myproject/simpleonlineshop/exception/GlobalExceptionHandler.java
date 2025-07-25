package com.myproject.simpleonlineshop.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// for any request going to any controller, that request is going to be passed in here if that method is going to throw any exception its going to be handle here
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex){
        String message = "You have not Permission to access this resource";
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);

    }
}
