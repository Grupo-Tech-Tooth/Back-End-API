package com.example.back.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacaoReq(
        @NotBlank(message = "O email não pode ser nulo")
                @Email(message = "Email inválido")
        String email,
        @NotBlank(message = "A senha não pode ser nula")
        String senha) {
}
