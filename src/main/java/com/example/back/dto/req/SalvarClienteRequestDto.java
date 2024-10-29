package com.example.back.dto.req;

import com.example.back.enums.Hierarquia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SalvarClienteRequestDto {

    // Getters e Setters

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

    @NotBlank
    @NotNull
    private String cpf;

    @NotBlank
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    private String senha;

    @NotNull
    private Hierarquia hierarquia;

}
