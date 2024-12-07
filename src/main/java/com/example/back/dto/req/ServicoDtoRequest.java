package com.example.back.dto.req;

import com.example.back.enums.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        @NotNull
        Categoria categoria
) {
}
