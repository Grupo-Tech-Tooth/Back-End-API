package com.example.back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "agenda_id", referencedColumnName = "id")
    private Agenda agenda;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @PrePersist
    public void verificarDisponibilidade() {
        // Lógica para verificar a disponibilidade da agenda antes de persistir o agendamento
        if (!agenda.getDisponibilidade().contains(dataHora)) {
            throw new IllegalArgumentException("Data e hora não disponíveis na agenda.");
        }
    }
}