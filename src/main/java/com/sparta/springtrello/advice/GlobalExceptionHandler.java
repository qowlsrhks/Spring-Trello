package com.sparta.springtrello.advice;

import com.sparta.springtrello.advice.exception.BadRequestException;
import com.sparta.springtrello.advice.exception.DataAccessException;
import com.sparta.springtrello.advice.exception.NotFoundException;
import com.sparta.springtrello.attachment.exception.FileBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 BadRequest Exception
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleFileEmptyException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
    }

    // 404 NotFound Exception
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleFileNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorMessage> handleDataAccessException(DataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(e.getMessage()));
    }
}
