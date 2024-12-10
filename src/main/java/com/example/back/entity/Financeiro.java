package com.example.back.entity;

import com.example.back.enums.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    @JsonManagedReference
    private Agendamento agendamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(name = "dataPagamento")
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "formaPagamento")
    private FormaPagamento formaPagamento;

    @Column(name = "parcelas")
    private Integer parcelas;
  
    @Column(name = "valorBruto")
    private Double valorBruto;

    @Column(name = "valorCorrigido")
    private Double valorCorrigido;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "taxa")
    private Double taxa;

    @Builder.Default
    @Column(name = "deletado", columnDefinition = "TINYINT(1)")
    private Boolean deletado = false;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;
}
