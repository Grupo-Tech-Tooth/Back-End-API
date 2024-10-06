package com.example.back.repository;

import com.example.back.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByCpf(String cpf);
    Optional<Medico> findByEmail(String email);
    List<Medico> findByNomeContainingOrSobrenomeContainingIgnoreCase(String nome, String sobrenome);

    Optional<Medico> findByCpfAndDeletadoFalse(String cpf);
    Optional<Medico> findByEmailAndDeletadoFalse(String email);

    List<Medico> findByDeletadoFalse();

    Optional<Medico> findByIdAndDeletadoFalse(Long id);

    List<Medico> findByDeletadoFalseAndNomeContainingOrSobrenomeContainingIgnoreCase(String nome, String sobrenome);
}
