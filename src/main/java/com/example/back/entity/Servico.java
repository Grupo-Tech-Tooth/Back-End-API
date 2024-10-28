package com.example.back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    public enum Tipo {
        consulta(1L, 20.0),
        limpeza(2L, 30.0),
        removerDente(3L, 50.0),;

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