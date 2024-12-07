package com.example.back.entity;

import com.example.back.enums.Hierarquia;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "senha")
    private String senha;

    @Column(name = "ativo", columnDefinition = "TINYINT(1)")
    private Boolean ativo = true;

    @Column(name = "deletado", columnDefinition = "TINYINT(1)")
    private Boolean deletado = false;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;

    @Enumerated(EnumType.STRING)
    @Column(name = "hierarquia")
    @Enumerated(EnumType.STRING)
    private Hierarquia hierarquia;

    @Column(name = "data_criacao", columnDefinition = "TIMESTAMP")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "funcionario_id")
    @JsonIgnoreProperties("loginInfo")
    private Funcionario funcionario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties("loginInfo")
    private Cliente cliente;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
