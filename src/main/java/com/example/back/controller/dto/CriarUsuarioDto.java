package com.example.back.controller.dto;

import com.example.back.entity.Usuario;
import jakarta.validation.constraints.NotBlank;

public record CriarUsuarioDto(
        @NotBlank String nome,
        @NotBlank String sobrenome,
        @NotBlank String email,
        @NotBlank String cpf,
        @NotBlank String senha
) {

    public Usuario toUsuario() {
        return new Usuario(nome, sobrenome, email, cpf, senha);
    }

}
