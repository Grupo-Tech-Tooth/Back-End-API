package com.example.back.dto.req;

import com.example.back.entity.Funcional;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class FuncionalRequestDto {

    @NotBlank(message = "Nome não pode ser vazio")
    private String nome;

    @NotBlank(message = "Sobrenome não pode ser vazio")
    private String sobrenome;

    @NotBlank(message = "Email não pode ser vazio")
    private String email;

    @CPF(message = "CPF inválido")
    @NotBlank
    private String cpf;

    @NotBlank(message = "Departamento não pode ser vazio")
    private String departamento;

    @NotBlank(message = "Senha não pode ser vazia")
    String senha;

    public Funcional toFuncional() {
        Funcional funcional = new Funcional();
        funcional.setNome(this.nome);
        funcional.setSobrenome(this.sobrenome);
        funcional.setCpf(this.cpf);
        funcional.setDepartamento(this.departamento);
        return funcional;
    }

}
