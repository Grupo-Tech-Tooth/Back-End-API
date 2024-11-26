package com.example.back.dto.req;

import java.time.LocalDateTime;

public record AgendamentoDTO(
        Long id,
        Long clienteId,
        Long medicoId,
        Long servicoId,
        String status,
        LocalDateTime dataHora,
        String cpf
){}
