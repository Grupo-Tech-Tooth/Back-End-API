package com.example.back.dto.req;

import com.example.back.enums.EspecializacaoOdontologica;
import com.example.back.strategy.Comissao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;

public class MedicoRequestDto {

    @NotBlank
    private String crm;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private EspecializacaoOdontologica especializacao;

    @NotBlank
    private Boolean ativo;

    @JsonIgnore
    private Comissao comissao;
}
