package com.example.back.dto.req;

import com.example.back.entity.Agendamento;
import com.example.back.entity.Medico;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AgendaRequestDto {

    @NotBlank
    private Medico medico;

    private List<LocalDateTime> disponibilidade;

    @NotBlank
    private List<Agendamento> agendamentos;

}
