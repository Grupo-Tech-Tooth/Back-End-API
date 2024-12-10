package com.example.back.dto.res;

import com.example.back.entity.Agenda;
import com.example.back.entity.Agendamento;
import com.example.back.entity.Cliente;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoResponseDto {

    private Cliente cliente;
    private Agenda agenda;
    private LocalDateTime dataHora;
    @NotBlank
    private String status;
    private String cpf;

    public static AgendamentoResponseDto converter(Agendamento agendamento) {
        return new AgendamentoResponseDto(
                agendamento.getCliente(),
                agendamento.getAgenda(),
                agendamento.getDataHora(),
                agendamento.getStatus(),
                agendamento.getCliente().getCpf()
        );
    }

    @PrePersist
    public void verificarDisponibilidade() {
        // Lógica para verificar a disponibilidade da agenda antes de persistir o agendamento
        if (!agenda.getDisponibilidade().contains(dataHora)) {
            throw new IllegalArgumentException("Data e hora não disponíveis na agenda.");
        }
    }
}
