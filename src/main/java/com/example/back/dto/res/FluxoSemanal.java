package com.example.back.dto.res;

import lombok.Data;

@Data
public class FluxoSemanal {
    Integer Domingo;
    Integer Segunda;
    Integer Terca;
    Integer Quarta;
    Integer Quinta;
    Integer Sexta;
    Integer Sabado;

    public FluxoSemanal(Integer domingo, Integer segunda, Integer terca, Integer quarta, Integer quinta, Integer sexta, Integer sabado) {
        Domingo = domingo;
        Segunda = segunda;
        Terca = terca;
        Quarta = quarta;
        Quinta = quinta;
        Sexta = sexta;
        Sabado = sabado;
    }
}
