package com.example.back.dto.req;

import com.example.back.entity.Medico;
import com.example.back.enums.EspecializacaoOdontologica;
import com.example.back.strategy.Comissao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicoRequestDto {

    @NotBlank
    private String nome;

    @NotBlank
    private String sobrenome;

    @NotBlank
    private String email;

    @NotBlank
    private String cpf;

    @NotBlank String senha;

    @NotBlank
    private String crm;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private EspecializacaoOdontologica especializacao;

    @NotBlank
    private Boolean ativo;

    public Medico toMedico() {
        Medico medico = new Medico();
        medico.setNome(this.nome);
        medico.setSobrenome(this.sobrenome);
        medico.setEmail(this.email);
        medico.setCpf(this.cpf);
        medico.setSenha(this.senha);
        medico.setCrm(this.crm);
        medico.setEspecializacao(this.especializacao);
        medico.setAtivo(this.ativo);
        return medico;
    }
}
