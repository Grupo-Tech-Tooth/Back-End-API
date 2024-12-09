package com.example.back.entity;

import com.example.back.enums.FormaPagamento;
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

    @Column(name = "data_consulta")
    private LocalDateTime dataConsulta;

    @ManyToOne
    @JoinColumn(name = "tratamento_principal", nullable = false)
    private Servico tratamentoPrincipal;

    @ManyToOne
    @JoinColumn(name = "tratamento_adicional")
    private Servico tratamentoAdicional;

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
