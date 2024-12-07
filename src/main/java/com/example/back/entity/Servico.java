package com.example.back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "servicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer duracaoMinutos;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "deletado", columnDefinition = "TINYINT(1)")
    private Boolean deletado = false;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;

    public enum Tipo {
        Consulta(1L, 20.0),
        Limpeza(2L, 30.0),
        Cirugia(3L, 50.0),
        Manutencao(4L, 40.0),
        Clareamento(5L, 60.0),
        Implante(6L, 70.0),
        Aparelho(7L, 80.0),
        RaioX(8L, 10.0),
        Canal(9L, 90.0);

        private Long id;
        private Double preco;

        Tipo(Long id, Double preco) {
            this.id = id;
            this.preco = preco;
        }

        public Servico getServico() {
            return Servico.builder()
                    .id(this.id)
                    .nome(this.name())
                    .duracaoMinutos(30)
                    .preco(BigDecimal.valueOf(this.preco))
                    .descricao("Descrição do serviço " + this.name())
                    .build();
        }

        public Long getId() {
            return this.id;
        }

        public Double getPreco() {
            return this.preco;
        }
    }
}