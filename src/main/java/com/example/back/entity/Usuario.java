package com.example.back.entity;

import com.example.back.dto.req.SalvarClienteRequestDto;
import com.example.back.enums.Hierarquia;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "sobrenome")
    private String sobrenome;

    @Column(name = "email")
    private String email;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "senha")
    private String senha;

    @Column(name = "ativo", columnDefinition = "TINYINT(1)")
    private Boolean ativo;

    @Column(name = "deletado")
    private Boolean deletado;

    @Column(name = "deletado_em")
    private LocalDate deletadoEm;

    @Column(name = "hierarquia")
    private Hierarquia hierarquia;

    public Usuario(SalvarClienteRequestDto dto) {
        this.nome = dto.getNome();
        this.sobrenome = dto.getSobrenome();
        this.email = dto.getEmail();
        this.cpf = dto.getCpf();
        this.senha = dto.getSenha();
        this.hierarquia = dto.getHierarquia();
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getDeletado() {
        return deletado;
    }

    public void setDeletado(Boolean deletado) {
        this.deletado = deletado;
    }

    public LocalDate getDeletadoEm() {
        return deletadoEm;
    }

    public void setDeletadoEm(LocalDate deletadoEm) {
        this.deletadoEm = deletadoEm;
    }

    public Hierarquia getHierarquia() {
        return hierarquia;
    }

    public void setHierarquia(Hierarquia hierarquia) {
        this.hierarquia = hierarquia;
    }

}
