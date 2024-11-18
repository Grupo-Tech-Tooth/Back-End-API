package com.example.back.entity;

import com.example.back.dto.req.SalvarClienteRequestDto;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
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

    @Column(name = "alergias")
    private List<String> alergias;

    @Column(name = "medicamentos")
    private List<String> medicamentos;

    @OneToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medicoResponsavel;

    @OneToOne
    @JoinColumn(name = "login_info_id")
    private LoginInfo loginInfo;

    public Cliente(SalvarClienteRequestDto dto) {
        this.setNome(dto.getNome());
        this.setSobrenome(dto.getSobrenome());
        this.setCpf(dto.getCpf());
         this.setGenero(dto.getGenero());
         this.setAlergias(dto.getAlergias());
         this.setMedicamentos(dto.getMedicamentos());
         this.setMedicoResponsavel(dto.getMedicoResponsavel());
    }
}
