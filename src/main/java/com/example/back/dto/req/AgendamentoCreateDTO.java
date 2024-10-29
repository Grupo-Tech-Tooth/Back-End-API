package com.example.back.dto.req;

import java.time.LocalDateTime;

public record AgendamentoCreateDTO(
        Long clienteId,
        Long medicoId,
        Long servicoId,
        String status,
        LocalDateTime dataHora
) {}