package com.example.back.dto.req;

import com.example.back.entity.Agenda;
import com.example.back.entity.Cliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class  AgendamentoRequestDto {

    @NotNull(message = "O cliente não pode ser nulo")
    private Cliente cliente;

    @NotNull(message = "A agenda não pode ser nula")
    private Agenda agenda;

    @NotNull(message = "A data e hora não podem ser nulas")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataHora;

    @NotBlank(message = "O status não pode estar em branco")
    private String status;

}
