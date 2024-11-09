package org.example.demo.toxiproxy.advice;

import org.example.demo.toxiproxy.dto.BasicResponseDto;
import org.example.demo.toxiproxy.service.exception.BadRequestException;
import org.example.demo.toxiproxy.service.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BasicResponseDto> handleGenericException(Exception e) {
        return new ResponseEntity<>(
                new BasicResponseDto("Internal server error. Please try again later"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BasicResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new BasicResponseDto("Invalid request body"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BasicResponseDto> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(new BasicResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BasicResponseDto> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(new BasicResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
