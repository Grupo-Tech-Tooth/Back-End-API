package com.example.back.dto.res;

import com.example.back.entity.Cliente;
import com.example.back.entity.Financeiro;
import com.example.back.entity.Medico;
import com.example.back.entity.Servico;
import com.example.back.enums.FormaPagamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinanceiroResponseDto {

    private Long id;
    private Long agendamentoId;
    private Long clienteId;
    private Long medicoId;
    private LocalDateTime dataPagamento;
    private FormaPagamento formaPagamento;
    private Integer parcelas;
    private Double valorBruto;
    private String observacao;
    private Double valorCorrigido;
    private Double taxa;
    private String especializacao;

    public static FinanceiroResponseDto converter(Financeiro financeiro) {
        return new FinanceiroResponseDto(
                financeiro.getId(),
                financeiro.getAgendamento().getId(),
                financeiro.getCliente().getId(),
                financeiro.getMedico().getId(),
                financeiro.getDataPagamento(),
                financeiro.getFormaPagamento(),
                financeiro.getParcelas(),
                financeiro.getValorBruto(),
                financeiro.getObservacao(),
                financeiro.getValorCorrigido(),
                financeiro.getTaxa(),
                financeiro.getMedico().getEspecializacao().name()
        );
    }

    public static List<FinanceiroResponseDto> converter(List<Financeiro> financeiros) {
        return financeiros.stream().map(FinanceiroResponseDto::converter).toList();
    }
}