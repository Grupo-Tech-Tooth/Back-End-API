package com.example.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Funcionario extends Usuario {

    @Column(name = "matricula")
    private String matricula;

    public Funcionario(String nome, String sobrenome, String email, String cpf, String senha, String matricula, Boolean ativo) {
        super(nome, sobrenome, email, cpf, senha, ativo);
        this.matricula = matricula;
    }
}
