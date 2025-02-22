package com.example.back.dto.res;

import com.example.back.entity.Funcional;
import com.example.back.enums.Hierarquia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionalResponseDto {
    private Long id;
    private String nome;
    private String sobrenome;
    private String email;
    private String cpf;
    private String departamento;
    private String matricula;
    private LocalDate dataNascimento;
    private String telefone;
    private String genero;
    private String cep;
    private String numeroResidencia;
    private String complemento;

    public FuncionalResponseDto(Funcional funcional) {
        this.id = funcional.getId();
        this.nome = funcional.getNome();
        this.sobrenome = funcional.getSobrenome();
        this.email = funcional.getLoginInfo().getEmail();
        this.cpf = funcional.getCpf();
        this.departamento = funcional.getDepartamento();
        this.matricula = funcional.getMatricula();
        this.dataNascimento = funcional.getDataNascimento();
        this.telefone = funcional.getTelefone();
        this.genero = funcional.getGenero();
        this.cep = funcional.getCep();
        this.numeroResidencia = funcional.getNumeroResidencia();
        this.complemento = funcional.getComplemento();
    }

    public static List<FuncionalResponseDto> converter(List<Funcional> funcionais) {
        return funcionais.stream().map(FuncionalResponseDto::new).toList();
    }

    public static FuncionalResponseDto converter(Funcional novoFuncional) {
        return new FuncionalResponseDto(novoFuncional);
    }
}
