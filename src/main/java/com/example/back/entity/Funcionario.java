package com.example.back.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "funcionario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "sobrenome")
    private String sobrenome;

    @Column(name = "especialidade")
    private String especialidade;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "senha")
    private String senha;

    public Funcionario(String nome, String sobrenome, String especialidade, String email, String cpf, String senha){
    }
}