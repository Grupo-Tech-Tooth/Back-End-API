package com.example.back.enums;

public enum Hierarquia {
    MEDICO("MEDICO"),
    FUNCIONAL("FUNCIONAL"),
    GERENTE("GERENTE"),
    CLIENTE("CLIENTE");

    private final String role;

    Hierarquia(String role) {
        this.role = role;
    }
}
