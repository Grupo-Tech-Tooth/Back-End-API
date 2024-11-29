package com.example.back.dto.req;

import com.example.back.enums.FormaPagamento;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class FinanceiroDtoRequest {
    @NotNull(message = "Data da consulta é obrigatório")
    LocalDateTime dataConsulta;

    @NotNull(message = "Nome do Paciente é obrigatório")
    String nomePaciente;

    @NotNull(message = "Nome do Médico é obrigatório")
    String medico;

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

    Double taxas;

    @CPF(message = "CPF inválido")
    @NotBlank (message = "CPF é obrigatório")
    String cpf;
}

