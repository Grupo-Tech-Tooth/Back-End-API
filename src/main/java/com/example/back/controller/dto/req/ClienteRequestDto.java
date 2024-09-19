package com.example.back.controller.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClienteRequestDto {

    // Getters e Setters
    @NotNull
    private String nome;
    @NotNull
    private String sobrenome;
    @NotNull
    private LocalDate dataNascimento;
    @NotNull
    private String genero;
}
