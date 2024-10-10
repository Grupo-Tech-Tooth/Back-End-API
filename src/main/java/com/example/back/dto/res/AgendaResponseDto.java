package com.example.back.dto.res;

import com.example.back.entity.Agendamento;
import com.example.back.entity.Medico;
import jakarta.persistence.*;
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
public class AgendaResponseDto {

    private Medico medico;

    private List<LocalDateTime> disponibilidade;

    private List<Agendamento> agendamentos;

}
