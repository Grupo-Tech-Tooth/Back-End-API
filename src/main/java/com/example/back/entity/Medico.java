package com.example.back.entity;

import com.example.back.enums.EspecializacaoOdontologica;
import com.example.back.strategy.Comissao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medico")
@Getter
@Setter
public class Medico extends Funcionario {

    @Column(name = "crm")
    private String crm;

    @Enumerated(EnumType.STRING)
    @Column(name = "especializacao")
    private EspecializacaoOdontologica especializacao;

}
