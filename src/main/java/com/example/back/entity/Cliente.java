package com.example.back.entity;

import com.example.back.dto.req.ClienteRequestDto;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "cliente")
@Getter
@Setter
public class Cliente extends Usuario {

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "genero")
    private String genero;

    public Cliente(ClienteRequestDto dto) {
        super(dto);
        this.dataNascimento = dto.getDataNascimento();
        this.genero = dto.getGenero();
    }
}
