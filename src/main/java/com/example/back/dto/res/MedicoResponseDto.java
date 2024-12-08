package com.example.back.dto.res;

import com.example.back.entity.Medico;
import com.example.back.enums.EspecializacaoOdontologica;
import com.example.back.enums.Hierarquia;
import com.example.back.strategy.Comissao;
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
    private String matricula;
    private String cep;
    private String numeroResidencia;
    private String complemento;
    private String crm;
    private EspecializacaoOdontologica especializacao;
    private Boolean ativo;
    private Comissao comissao;

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
                medico.getMatricula(),
                medico.getCep(),
                medico.getNumeroResidencia(),
                medico.getComplemento(),
                medico.getCrm(),
                medico.getEspecializacao(), // Enum EspecializacaoOdontologica já está no Medico
                medico.getLoginInfo().getAtivo(),
                medico.getComissao() // Assumindo que é do mesmo tipo da classe no DT
        );
    }

    public static List<MedicoResponseDto> converter(List<Medico> medicos) {
        return medicos.stream().map(MedicoResponseDto::converter).toList();
    }
}
