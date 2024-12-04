package com.example.back.dto.res;

import com.example.back.entity.Medico;
import com.example.back.enums.EspecializacaoOdontologica;
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
public class MedicoResponseDto {


    private Long id;
    private String nome;
    private String sobrenome;
    private String email;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String genero;
    private String cep;
    private String numeroResidencia;
    private String crm;
    private EspecializacaoOdontologica especializacao;
    private Boolean ativo;

    public static MedicoResponseDto converter(Medico medico) {
        return new MedicoResponseDto(
                medico.getId(),
                medico.getNome(),
                medico.getSobrenome(),
                medico.getLoginInfo().getEmail(),
                medico.getCpf(),
                medico.getDataNascimento(),
                medico.getTelefone(),
                medico.getGenero(),
                medico.getCep(),
                medico.getNumeroResidencia(),
                medico.getCrm(),
                medico.getEspecializacao(), // Enum EspecializacaoOdontologica já está no Medico
                medico.getLoginInfo().getAtivo()
        );
    }

    public static List<MedicoResponseDto> converter(List<Medico> medicos) {
        return medicos.stream().map(MedicoResponseDto::converter).toList();
    }
}
