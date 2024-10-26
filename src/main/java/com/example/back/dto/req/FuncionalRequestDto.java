package com.example.back.dto.req;

import com.example.back.entity.Funcional;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncionalRequestDto {

    @NotBlank
    private String nome;

    @NotBlank
    private String sobrenome;

    @NotBlank
    private String email;

    @NotBlank
    private String cpf;

    @NotBlank
    private String departamento;

    @NotBlank String senha;

    public Funcional toFuncional() {
        Funcional funcional = new Funcional();
        funcional.setNome(this.nome);
        funcional.setSobrenome(this.sobrenome);
        funcional.setEmail(this.email);
        funcional.setCpf(this.cpf);
        funcional.setDepartamento(this.departamento);
        funcional.setSenha(this.senha);
        return funcional;
    }

}
