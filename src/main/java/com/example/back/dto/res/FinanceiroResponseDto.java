package com.example.back.dto.res;

import com.example.back.entity.Cliente;
import com.example.back.entity.Financeiro;
import com.example.back.entity.Medico;
import com.example.back.enums.FormaPagamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinanceiroResponseDto {

    private Long id;
    private LocalDateTime dataConsulta;
    private Cliente cliente;
    private Medico medico;
    private LocalDateTime dataPagamento;
    private FormaPagamento formaPagamento;
    private Integer parcelas;
    private Double valorBruto;
    private Double valorCorrigido;
    private Double taxa;

    public static FinanceiroResponseDto converter(Financeiro financeiro) {
        return new FinanceiroResponseDto(
                financeiro.getId(),
                financeiro.getDataConsulta(),
                financeiro.getCliente(),
                financeiro.getMedico(),
                financeiro.getDataPagamento(),
                financeiro.getFormaPagamento(),
                financeiro.getParcelas(),
                financeiro.getValorBruto(),
                financeiro.getValorCorrigido(),
                financeiro.getTaxa()
        );
    }
}
