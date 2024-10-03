package com.example.back.entity;

import com.example.back.conversor.BooleanConverter;
import com.example.back.enums.EspecializacaoOdontologica;
import com.example.back.strategy.Comissao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medico extends Funcionario {

    @Column(name = "crm")
    private String crm;

    @Enumerated(EnumType.STRING)
    @Column(name = "especializacao")
    private EspecializacaoOdontologica especializacao;

    @Column(name = "ativo", columnDefinition = "BOOLEAN")
    @Convert(converter = BooleanConverter.class)
    private boolean ativo;

    @Transient
    @JsonIgnore
    private Comissao comissao;

    public double calcularComissao(double valorServico) {
        double comissaoBase = comissao.calcularComissao(valorServico);
        double percentualEspecializacao = especializacao.getPercentualComissao();
        return valorServico * (percentualEspecializacao / 100) + comissaoBase;
    }
}
