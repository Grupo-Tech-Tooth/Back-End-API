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

    @NotBlank(message = "O medico não pode ser nulo")
    private Medico medico;

    private List<LocalDateTime> disponibilidade;

    @NotBlank(message = "O status não pode estar em branco")
    private List<Agendamento> agendamentos;

}
