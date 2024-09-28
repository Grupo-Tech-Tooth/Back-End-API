package com.example.back.entity;

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

    public Cliente(String nome, String sobrenome, String email, String cpf, String senha, LocalDate dataNascimento, String genero, Boolean ativo) {
        super(nome, sobrenome, email, cpf, senha, ativo);
        this.dataNascimento = dataNascimento;
        this.genero = genero;
    }

    public Cliente(Cliente cliente) {
    }
}
