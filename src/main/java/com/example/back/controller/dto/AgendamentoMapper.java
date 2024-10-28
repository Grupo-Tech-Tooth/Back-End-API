package com.example.back.controller.dto;

import com.example.back.entity.*;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {
    public AgendamentoDTO toDTO(Agendamento agendamento) {
        return new AgendamentoDTO(
                agendamento.getId(),
                agendamento.getCliente().getId(),
                agendamento.getMedico().getId(),
                agendamento.getServico().getId(),
                agendamento.getStatus().getID(),
                agendamento.getDataHora()
        );
    }

    public Agendamento toEntity(AgendamentoCreateDTO dto, Cliente cliente, Medico medico, Servico servico, Agenda agenda) {
        return Agendamento.builder()
                .cliente(cliente)
                .medico(medico)
                .agenda(agenda)
                .servico(servico)
                .dataHora(dto.dataHora())
                .build();
    }
}