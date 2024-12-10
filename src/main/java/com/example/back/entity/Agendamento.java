package com.example.back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "agendamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    @JsonBackReference
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "agenda_id", nullable = false)
    private Agenda agenda;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private String status;


    @Column(name = "deletado", columnDefinition = "TINYINT(1)")
    private Boolean deletado = false;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;

    public void setCancelado(boolean b) {
    }
}