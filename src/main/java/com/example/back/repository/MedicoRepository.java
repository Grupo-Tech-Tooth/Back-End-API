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
    Optional<Medico> findByCpf(String cpf);
    Optional<Medico> findByEmail(String email);
    List<Medico> findByNomeContainingOrSobrenomeContainingIgnoreCase(String nome, String sobrenome);
    @Query("SELECT m FROM Medico m WHERE m.ativo = true AND m.id NOT IN " +
            "(SELECT a.medico.id FROM Agendamento a WHERE a.dataHora BETWEEN :inicio AND :fim)")
    List<Medico> findAvailableMedicos(LocalDateTime inicio, LocalDateTime fim);
}
