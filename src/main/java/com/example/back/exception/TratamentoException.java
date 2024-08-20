package com.example.back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class TratamentoException extends RuntimeException{

    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problemDetail.setTitle("Erro Interno");

        return problemDetail;
    }

}
