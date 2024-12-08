package com.example.back.dto.req;

import com.example.back.dto.res.ClienteResponseDto;
import com.example.back.dto.res.MedicoResponseDto;
import com.example.back.entity.Cliente;
import com.example.back.entity.Medico;
import com.example.back.entity.Servico;

import java.time.LocalDateTime;

public record AgendamentoDTO(
        Long id,
        ClienteResponseDto cliente,
        MedicoResponseDto medico,
        Servico servico,
        String status,
        LocalDateTime dataHora
) {
    public AgendamentoDTO(Long id, Cliente cliente, Medico medico, Servico servico, String status, LocalDateTime dataHora) {
        this(id, ClienteResponseDto.converter(cliente), MedicoResponseDto.converter(medico), servico, status, dataHora);
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}
