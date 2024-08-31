package com.example.back.controller.dto;

import com.example.back.entity.Usuario;

public record UsuarioDto(
        String nome,
        String sobrenome,
        String email,
        String cpf
) {

    public static UsuarioDto fromUsuario(Usuario usuario) {
        return new UsuarioDto(
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getCpf()
        );
    }

}
