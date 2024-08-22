package com.example.back.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(UsuarioExistenteException.class)
    public ResponseEntity<ErrorResponseDto> usuarioException(UsuarioExistenteException exception, HttpServletResponse response){
        ErrorResponseDto erro = new ErrorResponseDto();
        erro.setMessage(exception.getMessage());
        erro.setStatusCode(HttpStatus.NOT_FOUND.value());
        erro.setTime(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

}
