package com.example.back.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionalResponseDto {
    private String nome;
    private String sobrenome;
    private String email;
    private String cpf;
    private String departamento;
    private LocalDate dataNascimento;
    private String telefone;
    private String genero;
    private String cep;
    private String numeroResidencia;

}
