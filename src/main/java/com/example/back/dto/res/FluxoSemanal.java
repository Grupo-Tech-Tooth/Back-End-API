package com.example.back.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "Domingo", "Segunda", "Terca", "Quarta", "Quinta", "Sexta", "Sabado" })
public class FluxoSemanal {
    @JsonProperty("Domingo")
    Integer Domingo;

    @JsonProperty("Segunda")
    Integer Segunda;

    @JsonProperty("Terça")
    Integer Terca;

    @JsonProperty("Quarta")
    Integer Quarta;

    @JsonProperty("Quinta")
    Integer Quinta;

    @JsonProperty("Sexta")
    Integer Sexta;

    @JsonProperty("Sabado")
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
