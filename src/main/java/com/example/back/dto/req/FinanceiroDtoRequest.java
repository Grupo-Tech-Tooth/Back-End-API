package com.example.back.dto.req;
import com.example.back.enums.FormaPagamento;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FinanceiroDtoRequest {
    @NotNull(message = "Id do Agendamento é obrigatório")
    Long idAgendamento;

    @NotNull(message = "Id do Paciente é obrigatório")
    Long idPaciente;

    @NotNull(message = "Id do Médico é obrigatório")
    Long idMedico;

    @NotNull(message = "Data do pagamento é obrigatória")
    LocalDateTime dataPagamento;

    @NotNull(message = "Forma de pagamento é obrigatória")
    FormaPagamento formaPagamento;

    @Positive(message = "Valor de parcelas deve ser positivo")
    @NotNull(message = "Número de parcelas é obrigatório")
    Integer parcelas;

    @Positive(message = "Valor da consulta deve ser positivo e maior que zero")
    @NotNull(message = "Valor da consulta é obrigatório")
        Double valorBruto;

    String observacao;

    Double taxas;

}

