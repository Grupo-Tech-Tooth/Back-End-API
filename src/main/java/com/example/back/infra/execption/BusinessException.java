package com.example.back.infra.execption;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}