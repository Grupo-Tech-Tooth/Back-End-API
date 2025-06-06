package com.example.back.entity;

import com.example.back.dto.req.SalvarClienteRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends Usuario {

    @Column(name = "genero")
    private String genero;

    @Lob
    @Column(name = "alergias", columnDefinition = "TEXT")
    private String alergias;

    @Lob
    @Column(name = "medicamentos", columnDefinition = "TEXT")
    private String medicamentos;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Lob
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @OneToOne
    @JoinColumn(name = "login_info_id")
    private LoginInfo loginInfo;

}