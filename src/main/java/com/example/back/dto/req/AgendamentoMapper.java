package com.example.back.dto.req;

import com.example.back.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AgendamentoMapper {
    public static AgendamentoDTO toDTO(Agendamento agendamento) {
        return new AgendamentoDTO(
                agendamento.getId(),
                agendamento.getCliente().getId(),
                agendamento.getMedico().getId(),
                agendamento.getServico().getId(),
                agendamento.getStatus(),
                agendamento.getDataHora(),
                agendamento.getCliente().getCpf()
        );
    }

    public static Agendamento toEntity(AgendamentoCreateDTO dto, Cliente cliente, Medico medico, Servico servico, Agenda agenda) {
        return Agendamento.builder()
                .cliente(cliente)
                .medico(medico)
                .agenda(agenda)
                .servico(servico)
                .dataHora(dto.dataHora())
                .status(dto.status())
                .cpf(dto.cpf())
                .build();
    }

    static public List<AgendamentoDTO> converter(List<Agendamento> agendamentos) {
        return agendamentos.stream().map(AgendamentoMapper::toDTO).collect(Collectors.toList());
    }
}