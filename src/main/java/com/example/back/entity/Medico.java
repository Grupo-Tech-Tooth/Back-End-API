package com.example.back.entity;

import com.example.back.enums.EspecializacaoOdontologica;
import com.example.back.strategy.Comissao;
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

    @Transient
    private Comissao comissaoStrategy;

    public Medico(String nome, String sobrenome, String email, String cpf, String senha, String matricula, String crm, Comissao comissaoStrategy, EspecializacaoOdontologica especializacao) {
        super(nome, sobrenome, email, cpf, senha, matricula);
        this.crm = crm;
        this.comissaoStrategy = comissaoStrategy;
        this.especializacao = especializacao;
    }

    public double calcularComissao(double valorServico) {
        double comissaoBase = comissaoStrategy.calcularComissao(valorServico);
        double percentualEspecializacao = especializacao.getPercentualComissao();
        return valorServico * (percentualEspecializacao / 100) + comissaoBase;
    }
}
