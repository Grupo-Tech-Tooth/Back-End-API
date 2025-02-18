package com.example.back.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "disponibilidade", joinColumns = @JoinColumn(name = "agenda_id"))
    @Column(name = "disponibilidade")
    private List<LocalDateTime> disponibilidade;


    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Agendamento> agendamentos = new ArrayList<>();
}