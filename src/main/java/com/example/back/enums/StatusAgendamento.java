package com.example.back.enums;

public enum StatusAgendamento {
    AGENDADO("Agendado"),
    CANCELADO("Cancelado"),
    PENDENTE("Pendente"),
    REMARCADO("Remarcado");

    private final String status;

    StatusAgendamento(
        String status
    ) {
        this.status = status;
    }
}
