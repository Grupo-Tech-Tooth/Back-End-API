package com.example.back.controller.dto;

import java.time.LocalDateTime;

public record AgendamentoDTO(
        Long id,
        Long clienteId,
        Long medicoId,
        Long servicoId,
        LocalDateTime dataHora
) {}