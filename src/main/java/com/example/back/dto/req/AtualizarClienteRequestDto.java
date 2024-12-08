package com.example.back.dto.req;

import com.example.back.entity.Cliente;
import com.example.back.entity.Medico;
import com.example.back.enums.Hierarquia;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class AtualizarClienteRequestDto {

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

    @NotBlank(message = "Telefone não pode ser vazio")
    @NotNull(message = "Telefone não pode ser nulo")
    private String telefone;

    @NotBlank(message = "CEP não pode ser vazio")
    @NotNull(message = "CEP não pode ser nulo")
    private String cep;

    @NotBlank(message = "Número de Residência não pode ser vazio")
    @NotNull(message = "Número de Residência não pode ser nulo")
    private String numeroResidencia;

    private String alergias;

    private String medicamentos;

    private Long medicoId;

    private String observacoes;

}
