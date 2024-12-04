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

    @Column(name = "medico_id", nullable = false)
    private Long medicoResponsavelId;

    @Lob
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "ultimo_agendamento")
    private LocalDate ultimoAgendamento;

    @OneToOne
    @JoinColumn(name = "login_info_id")
    private LoginInfo loginInfo;

    public Cliente(SalvarClienteRequestDto dto) {
        this.setNome(dto.getNome());
        this.setSobrenome(dto.getSobrenome());
        this.setCpf(dto.getCpf());
        this.setCep(dto.getCep());
        this.setNumeroResidencia(dto.getNumeroResidencia());
        this.setDataNascimento(dto.getDataNascimento());
        this.setGenero(dto.getGenero());
        this.setAlergias(dto.getAlergias());
        this.setMedicamentos(dto.getMedicamentos());
        this.setMedicoResponsavelId(dto.getMedicoResponsavelId());
        this.setUltimoAgendamento(dto.getUltimoAgendamento());
        this.setObservacoes(dto.getObservacoes());
    }
}