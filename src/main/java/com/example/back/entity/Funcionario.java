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
}
