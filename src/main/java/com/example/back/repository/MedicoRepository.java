package com.example.back.repository;

import com.example.back.entity.Medico;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByLoginInfoEmail(String email);
    Optional<Medico> findByLoginInfoFuncionarioCpf(String cpf);

    List<Medico> findByNomeContainingOrSobrenomeContainingIgnoreCase(String nome, String sobrenome);

    @Query("SELECT m FROM Medico m JOIN m.loginInfo li WHERE li.ativo = true AND m.id NOT IN " +
            "(SELECT a.medico.id FROM Agendamento a WHERE a.dataHora BETWEEN :inicio AND :fim)")
    List<Medico> findAvailableMedicos(LocalDateTime inicio, LocalDateTime fim);

    Optional<Medico> findByLoginInfoFuncionarioCpfAndLoginInfo_DeletadoFalse(String cpf);
    Optional<Medico> findByLoginInfoEmailAndLoginInfo_DeletadoFalse(String email);

    List<Medico> findByLoginInfo_DeletadoFalse();

    Optional<Medico> findByIdAndLoginInfo_DeletadoFalse(Long id);

    List<Medico> findByLoginInfo_DeletadoFalseAndNomeContainingOrSobrenomeContainingIgnoreCase(String nome, String sobrenome);

    List<Medico> findByLoginInfo_DeletadoFalseAndLoginInfo_EmailContainingIgnoreCase(String email);

    List<Medico> findByLoginInfo_DeletadoFalseAndCpfContainingIgnoreCase(String cpf);

    @Query("SELECT m.id FROM Medico m WHERE m.loginInfo.deletado = false AND LOWER(m.cpf) LIKE LOWER(CONCAT('%', :cpf, '%'))")
    Optional<Long> findIdByLoginInfo_DeletadoFalseAndCpfContainingIgnoreCase(@Param("cpf") String cpf);

    Optional<Object> findByIdAndLoginInfoDeletadoFalse(Long medicoId);
}
