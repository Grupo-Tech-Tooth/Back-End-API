package com.example.back.exception;


import java.time.LocalDateTime;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
