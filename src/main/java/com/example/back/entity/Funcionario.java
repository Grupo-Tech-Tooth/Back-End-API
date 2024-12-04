package com.example.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Funcionario extends Usuario {

    @Column(name = "matricula")
    private String matricula;

    @OneToOne
    @JoinColumn(name = "login_info_id")
    private LoginInfo loginInfo;

    @Column(name = "complemento")
    private String complemento;

}
