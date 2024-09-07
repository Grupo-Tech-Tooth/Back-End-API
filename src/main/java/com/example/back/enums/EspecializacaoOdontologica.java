package com.example.back.enums;

public enum EspecializacaoOdontologica {
    ORTODONTIA(10.0),
    PERIODONTIA(12.0),
    ENDODONTIA(15.0),
    CIRURGIA_BUCO_MAXILO(20.0),
    IMPLANTODONTIA(18.0),
    PROTESE_DENTARIA(10.0),
    ODONTOLOGIA_ESTETICA(8.0),
    ODONTO_PEDIATRIA(10.0);

    private final double percentualComissao;

    EspecializacaoOdontologica(double percentualComissao) {
        this.percentualComissao = percentualComissao;
    }

    public double getPercentualComissao() {
        return percentualComissao;
    }
}