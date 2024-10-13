package com.example.back.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AtualizarClienteRequestDto {
    @NotBlank
    @NotNull
    private String nome;

    @NotBlank
    @NotNull
    private String sobrenome;

    @NotNull
    private LocalDate dataNascimento;

    @NotBlank
    @NotNull
    private String genero;
}
