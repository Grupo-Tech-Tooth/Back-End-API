package com.example.back.controller.dto;

import com.example.back.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record AtualizarUsuarioDto(
        String nome,
        String sobrenome,
        @Email String email,
        @Size(min = 11, max = 11) String cpf,
        String senha
) {

    public Usuario toUsuario(Usuario usuario) {
        if (nome != null) usuario.setNome(nome);
        if (sobrenome != null) usuario.setSobrenome(sobrenome);
        if (email != null) usuario.setEmail(email);
        if (cpf != null) usuario.setCpf(cpf);
        if (senha != null) usuario.setSenha(senha);
        return usuario;
    }

}
