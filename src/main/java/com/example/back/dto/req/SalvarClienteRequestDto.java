package com.example.back.dto.req;

import com.example.back.enums.Hierarquia;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class SalvarClienteRequestDto {

    // Getters e Setters

    @NotBlank(message = "Nome não pode ser vazio")
    @NotNull(message = "Nome não pode ser nulo")
    private String nome;

    @NotBlank(message = "Sobrenome não pode ser vazio")
    @NotNull(message = "Sobrenome não pode ser nulo")
    private String sobrenome;

    @NotNull(message = "Data de nascimento não pode ser nula")
    private LocalDate dataNascimento;

    @NotBlank
    @NotNull(message = "Gênero não pode ser nulo")
    private String genero;

    @NotBlank(message = "CPF não pode ser vazio")
    @NotNull(message = "CPF não pode ser nulo")
    @CPF(message = "CPF inválido")
    private String cpf;

    @NotBlank(message = "Email não pode ser vazio")
    @NotNull(message = "Email não pode ser nulo")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha não pode ser vazia")
    @NotNull
    private String senha;

    @NotNull(message = "Hierarquia não pode ser nula")
    private Hierarquia hierarquia;

}
