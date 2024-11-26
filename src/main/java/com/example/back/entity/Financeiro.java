package com.example.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.Date;

@Entity
@Table(name = "financeiro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Financeiro{

    @Column(name = "id")
    private Long id;
    @Column(name = "data_consulta")
    private Date dataConsulta;
    @Column(name = "nomePaciente")
    private String nomePaciente;
    @Column(name = "medico")
    private String medico;
    @Column(name = "dataPagamento")
    private Date dataPagamento;
    @Column(name = "formaPagamento")
    private String formaPagamento;
    @Column(name = "valor")
    private DecimalFormat valor;

    @Column(name = "cpf")
    private String cpf;

    public Financeiro(Long id, Date dataConsulta, String nomePaciente, String medico, Date dataPagamento, String formaPagamento, DecimalFormat valor, String cpf) {
        this.id = id;
        this.dataConsulta = dataConsulta;
        this.nomePaciente = nomePaciente;
        this.medico = medico;
        this.dataPagamento = dataPagamento;
        this.formaPagamento = formaPagamento;
        this.valor = valor;
        this.cpf = cpf;
    }

}
