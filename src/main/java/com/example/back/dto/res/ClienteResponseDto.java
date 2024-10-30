package com.example.back.dto.res;

import com.example.back.entity.Cliente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDto {

    // Getters e Setters
    private String nome;
    private String sobrenome;
    private String email;
    private String cpf;
    private LocalDate dataNascimento;
    private String genero;

    public ClienteResponseDto(Cliente cliente) {

        this.nome = cliente.getNome();
        this.sobrenome = cliente.getSobrenome();
        this.email = cliente.getLoginInfo().getEmail();
        this.cpf = cliente.getCpf();
        this.dataNascimento = cliente.getDataNascimento();
        this.genero = cliente.getGenero();

    }

}
