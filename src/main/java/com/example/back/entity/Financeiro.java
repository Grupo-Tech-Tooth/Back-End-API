package com.example.back.entity;

import com.example.back.enums.FormaPagamento;
import jakarta.persistence.*;
import lombok.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "financeiro")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Financeiro{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data_consulta")
    private LocalDateTime dataConsulta;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(name = "dataPagamento")
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "formaPagamento")
    private FormaPagamento formaPagamento;

    @Column(name = "parcelas")
    private Integer parcelas;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "deletado", columnDefinition = "TINYINT(1)")
    private Boolean deletado = false;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;
}
