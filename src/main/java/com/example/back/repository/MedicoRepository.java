package com.example.back.repository;

import com.example.back.entity.Medico;
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
}
