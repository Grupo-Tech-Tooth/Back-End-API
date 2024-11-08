package com.example.back.entity;

import com.example.back.dto.req.SalvarClienteRequestDto;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends Usuario {

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "genero")
    private String genero;

    @OneToOne
    @JoinColumn(name = "login_info_id")
    private LoginInfo loginInfo;

    public Cliente(SalvarClienteRequestDto dto) {
        this.setNome(dto.getNome());
        this.setSobrenome(dto.getSobrenome());
        this.setCpf(dto.getCpf());
        this.setDataNascimento(dto.getDataNascimento());
        this.setGenero(dto.getGenero());
    }
}
