package com.example.back.dto.req;

import com.example.back.entity.Agenda;
import com.example.back.entity.Cliente;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class AgendamentoRequestDto {
    @NotBlank
    private Cliente cliente;

    @NotBlank
    private Agenda agenda;

    @NotBlank
    private LocalDateTime dataHora;

    @NotBlank
    private String status;

}
