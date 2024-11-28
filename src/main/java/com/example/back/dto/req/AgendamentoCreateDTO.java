package com.example.back.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record AgendamentoCreateDTO(

        @Positive
        @NotNull(message = "O id do cliente não pode ser nulo")
        Long clienteId,

        @Positive
        @NotNull(message = "O id do medico não pode ser nulo")
        Long medicoId,

        @Positive
        @NotNull(message = "O id do servico não pode ser nulo")
        Long servicoId,

        String status,

        @NotNull(message = "A data e hora não pode ser nula")
        LocalDateTime dataHora,

        @NotNull(message = "O CPF não pode ser nulo")
        String cpf
) {}