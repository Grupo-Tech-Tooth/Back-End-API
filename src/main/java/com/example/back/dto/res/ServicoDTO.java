package com.example.back.dto.res;

import java.util.Objects;

public class ServicoDTO {

    String nome;
    Integer usos;

    public ServicoDTO() {
    }

    public ServicoDTO(String nome, Integer usos) {
        this.nome = nome;
        this.usos = usos;
    }

    public String getNome() {
        return nome;
    }

    public Integer getUsos() {
        return usos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUsos(Integer usos) {
        this.usos = usos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServicoDTO that = (ServicoDTO) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}
