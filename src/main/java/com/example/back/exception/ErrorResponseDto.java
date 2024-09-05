package com.example.back.exception;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorResponseDto {

    private String message;
    private Integer statusCode;
    private LocalDateTime time;

    public ErrorResponseDto() {
    }

    public ErrorResponseDto(String message, Integer statusCode, LocalDateTime time) {
        this.message = message;
        this.statusCode = statusCode;
        this.time = time;
    }

}