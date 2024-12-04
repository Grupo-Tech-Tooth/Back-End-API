package com.example.back.enums;

public enum EspecializacaoOdontologica {
    ORTODONTIA(10.0, "Ortodontia"),
    PERIODONTIA(12.0, "Periodontia"),
    ENDODONTIA(15.0, "Endodontia"),
    CIRURGIA_BUCO_MAXILO(20.0, "Cirurgia buco maximo"),
    IMPLANTODONTIA(18.0, "Implantodontia"),
    PROTESE_DENTARIA(10.0, "Protese dentaria"),
    ODONTOLOGIA_ESTETICA(8.0, "Odontologia estetica"),
    ODONTO_PEDIATRIA(10.0, "Odonto pediatria"),;

    private final double percentualComissao;
    private final String label;

    EspecializacaoOdontologica(double percentualComissao, String label) {
        this.percentualComissao = percentualComissao;
        this.label = label;
    }

    public double getPercentualComissao() {
        return percentualComissao;
    }

    public String getLabel() {
        return label;
    }
}