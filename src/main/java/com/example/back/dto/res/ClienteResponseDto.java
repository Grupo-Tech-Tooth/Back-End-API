package com.example.back.dto.res;

import com.example.back.dto.req.AgendamentoDTO;
import com.example.back.entity.Agendamento;
import com.example.back.entity.Cliente;
import com.example.back.entity.Medico;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDto {

    private Long id;
    private String nome;
    private String sobrenome;
    private String email;
    private String cpf;
    private LocalDate dataNascimento;
    private String genero;
    private String telefone;
    private String cep;
    private String numeroResidencia;
    private String alergias;
    private String medicamentos;
    private Long medicoId;
    private AgendamentoDTO ultimoAgendamento;
    private String observacoes;

    public ClienteResponseDto(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.sobrenome = cliente.getSobrenome();
        this.email = cliente.getLoginInfo().getEmail();
        this.cpf = cliente.getCpf();
        this.telefone = cliente.getTelefone();
        this.cep = cliente.getCep();
        this.numeroResidencia = cliente.getNumeroResidencia();
        this.alergias = cliente.getAlergias();
        this.medicamentos = cliente.getMedicamentos();
        this.medicoId = cliente.getMedico() != null ? cliente.getMedico().getId() : null;
        this.dataNascimento = cliente.getDataNascimento();
        this.genero = cliente.getGenero();
        this.observacoes = cliente.getObservacoes();
    }

    public static List<ClienteResponseDto> converter(List<Cliente> clientes) {
        return clientes.stream().map(ClienteResponseDto::new).toList();
    }

    public static ClienteResponseDto converter(Cliente cliente) {
        return new ClienteResponseDto(cliente);
    }
}