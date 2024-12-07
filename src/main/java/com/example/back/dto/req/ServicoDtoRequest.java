package com.example.back.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ServicoDtoRequest(
        @NotBlank
        String nome,
        @Positive
        Integer duracaoMinutos,
        @Positive
        Double preco,
        @NotBlank
        String descricao,
        @NotBlank
        String categoria
) {
}
