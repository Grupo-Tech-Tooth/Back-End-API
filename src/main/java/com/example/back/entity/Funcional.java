package com.example.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "funcional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Funcional extends Funcionario {

    @Column(name = "departamento")
    private String departamento;
}
