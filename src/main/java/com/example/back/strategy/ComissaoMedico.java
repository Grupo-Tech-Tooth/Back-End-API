package com.example.back.strategy;

public class ComissaoMedico implements Comissao {

    private final double percentualComissao;

    public ComissaoMedico(double percentualComissao) {
        this.percentualComissao = percentualComissao;
    }

    @Override
    public double calcularComissao(double valorServico) {
        return valorServico * (percentualComissao / 100);
    }
}