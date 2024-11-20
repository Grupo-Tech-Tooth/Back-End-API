package com.example.back.strategy;

public class ComissaoMedico implements Comissao {

    private final Double percentualComissao;

    public ComissaoMedico(double percentualComissao) {
        this.percentualComissao = percentualComissao;
    }

    @Override
    public Double calcularComissao(double valorServico) {
        return valorServico * (percentualComissao / 100);
    }
}