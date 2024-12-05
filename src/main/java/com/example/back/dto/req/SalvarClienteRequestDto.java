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
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SalvarClienteRequestDto {

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

    private Hierarquia hierarquia;

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

    private Long medicoResponsavelId;

    private String medicoResponsavel;

    private Long medicoId;

    private String observacoes;

    public Cliente toCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome(this.nome);
        cliente.setSobrenome(this.sobrenome);
        cliente.setCpf(this.cpf);
        cliente.setDataNascimento(this.dataNascimento);
        cliente.setTelefone(this.telefone);
        cliente.setGenero(this.genero);
        cliente.setCep(this.cep);
        cliente.setNumeroResidencia(this.numeroResidencia);
        cliente.setAlergias(this.alergias);
        cliente.setMedicamentos(this.medicamentos);
        cliente.setMedicoResponsavelId(this.medicoResponsavelId);
        cliente.setObservacoes(this.observacoes);
        return cliente;
    }
}