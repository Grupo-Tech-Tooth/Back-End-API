package com.example.back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agendas")

@Getter
@Setter
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ElementCollection
    @CollectionTable(name = "disponibilidade", joinColumns = @JoinColumn(name = "agenda_id"))
    @Column(name = "disponibilidade")
    private List<LocalDateTime> disponibilidade;

    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Agendamento> agendamentos = new ArrayList<>();

    public DayOfWeek getDiaSemana() {
        // Supondo que a disponibilidade contenha datas e horas, retornamos o dia da semana do primeiro item
        return disponibilidade.isEmpty() ? null : disponibilidade.get(0).getDayOfWeek();
    }
}