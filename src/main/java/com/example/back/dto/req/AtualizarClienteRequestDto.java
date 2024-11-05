package com.example.back.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AtualizarClienteRequestDto {

    @NotBlank(message = "O nome não pode ser nulo")
    @NotNull(message = "O nome não pode ser nulo")
    private String nome;

    @NotBlank(message = "O sobrenome não pode ser nulo")
    @NotNull(message = "O sobrenome não pode ser nulo")
    private String sobrenome;

    @NotNull(message = "A data de nascimento não pode ser nula")
    private LocalDate dataNascimento;

    @NotBlank(message = "O gênero não pode ser nulo")
    @NotNull(message = "O gênero não pode ser nulo")
    private String genero;
}
