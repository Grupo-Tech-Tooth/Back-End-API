package com.example.back.infra.execption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UsuarioExistenteException extends TratamentoException{

    private String detalhe;

    public UsuarioExistenteException(String detalhe) {
        this.detalhe = detalhe;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        problemDetail.setTitle("Usuario já existente");
        problemDetail.setDetail(detalhe);

        return problemDetail;
    }

}
