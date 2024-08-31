package com.example.back.controller.dto;

import com.example.back.entity.Funcionario;

public record FuncionarioDto(
        String nome,
        String sobrenome,
        String especialidade,
        String email,
        String cpf
) {
    public static FuncionarioDto fromFuncionario(Funcionario funcionario) {
        return new FuncionarioDto(
                funcionario.getNome(),
                funcionario.getSobrenome(),
                funcionario.getEspecialidade(),
                funcionario.getEmail(),
                funcionario.getCpf()
        );
    }

}