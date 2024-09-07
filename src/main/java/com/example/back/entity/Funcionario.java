package com.example.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "funcionario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario extends Usuario {

    @Column(name = "matricula")
    private String matricula;

    public Funcionario(String nome, String sobrenome, String email, String cpf, String senha, String matricula) {
        super(nome, sobrenome, email, cpf, senha);
        this.matricula = matricula;
    }
}
