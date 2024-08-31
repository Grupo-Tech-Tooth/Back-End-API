package com.example.back.controller.dto;

import com.example.back.entity.Funcionario;
import jakarta.validation.constraints.NotBlank;

public record CriarFuncionarioDto(
        @NotBlank String nome,
        @NotBlank String sobrenome,
        @NotBlank String email,
        @NotBlank String especialidade,
        @NotBlank String cpf,
        @NotBlank String senha
) {

    public Funcionario toFuncionario() { return new Funcionario(nome, sobrenome, especialidade, email, cpf, senha); }

}
