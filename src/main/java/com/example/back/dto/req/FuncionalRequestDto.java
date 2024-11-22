package com.example.back.dto.req;

import com.example.back.entity.Funcional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FuncionalRequestDto {

    @NotBlank(message = "Nome não pode ser vazio")
    private String nome;

    @NotBlank(message = "Sobrenome não pode ser vazio")
    private String sobrenome;

    @NotBlank(message = "Email não pode ser vazio")
    private String email;

    @CPF(message = "CPF inválido")
    @NotBlank(message = "CPF não pode ser vazio")
    private String cpf;

    @NotBlank(message = "Departamento não pode ser vazio")
    private String departamento;

    @NotBlank(message = "Senha não pode ser vazia")
    String senha;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    @NotBlank(message = "Gênero é obrigatório")
    private String genero;

    @NotBlank(message = "Cep é obrigatório")
    private String cep;

    private Integer numeroResidencia;

    public Funcional toFuncional() {
        Funcional funcional = new Funcional();
        funcional.setNome(this.nome);
        funcional.setSobrenome(this.sobrenome);
        funcional.setCpf(this.cpf);
        funcional.setDepartamento(this.departamento);
        funcional.setDataNascimento(this.dataNascimento);
        funcional.setTelefone(this.telefone);
        funcional.setGenero(this.genero);
        funcional.setCep(this.cep);
        funcional.setNumeroResidencia(this.numeroResidencia);
        return funcional;
    }

}
