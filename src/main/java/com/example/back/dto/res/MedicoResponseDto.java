package com.example.back.dto.res;

import com.example.back.entity.Medico;
import com.example.back.enums.EspecializacaoOdontologica;
import com.example.back.strategy.Comissao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicoResponseDto {

    private String nome;
    private String sobrenome;
    private String email;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String genero;
    private String cep;
    private String numeroResidencia;
    private String crm;
    private EspecializacaoOdontologica especializacao;
    private Boolean ativo;
    private Comissao comissao;

    public double calcularComissao(double valorServico) {
        double comissaoBase = comissao.calcularComissao(valorServico);
        double percentualEspecializacao = especializacao.getPercentualComissao();
        return valorServico * (percentualEspecializacao / 100) + comissaoBase;
    }

    public static MedicoResponseDto converter(Medico medico) {
        return new MedicoResponseDto(
                medico.getNome(),
                medico.getSobrenome(),
                medico.getLoginInfo().getEmail(),
                medico.getCpf(),
                medico.getDataNascimento(),
                medico.getTelefone(),
                medico.getGenero(),
                medico.getCep(),
                medico.getNumeroResidencia(),
                medico.getCrm(),
                medico.getEspecializacao(), // Enum EspecializacaoOdontologica já está no Medico
                medico.getLoginInfo().getAtivo(),
                medico.getComissao() // Assumindo que é do mesmo tipo da classe no DTO
        );
    }
}
