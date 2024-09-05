package com.example.back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "agenda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "medico_id", referencedColumnName = "id")
    private Medico medico;

    @ElementCollection
    @CollectionTable(name = "disponibilidade", joinColumns = @JoinColumn(name = "agenda_id"))
    @Column(name = "disponibilidade")
    private List<LocalDateTime> disponibilidade;

    @OneToMany(mappedBy = "agenda")
    private List<Agendamento> agendamentos;

    public List<LocalDateTime> getDisponibilidade() {
        return disponibilidade;
    }
}