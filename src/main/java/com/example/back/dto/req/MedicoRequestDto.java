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
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class MedicoRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Sobrenome é obrigatório")
    private String sobrenome;

    @NotBlank(message = "Email é obrigatório")
    private String email;

    @CPF(message = "CPF inválido")
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "Senha é obrigatória")
    String senha;

    @NotBlank(message = "CRM é obrigatório")
    private String crm;

    @Enumerated(EnumType.STRING)
    private EspecializacaoOdontologica especializacao;

    public Medico toMedico() {
        Medico medico = new Medico();
        medico.setNome(this.nome);
        medico.setSobrenome(this.sobrenome);
        medico.setCpf(this.cpf);
        medico.setCrm(this.crm);
        medico.setEspecializacao(this.especializacao);
        return medico;
    }
}
