package com.example.back.enums;

public enum FormaPagamento {
    PIX("Pix"),
    DINHEIRO("Dinheiro Físico"),
    CHEQUE("Cheque"),
    CARTAO_DEBITO("Cartão de Débito"),
    CARTAO_CREDITO("Cartão de Crédito"),
    PERMUTA("Permuta");


    private final String label;

    private FormaPagamento(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
