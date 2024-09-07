package com.example.back.repository;

import com.example.back.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByEmail(String email);
    Optional<Funcionario> findByCpf(String cpf);
    List<Funcionario> findByNomeContainingOrSobrenomeContaining(String nome, String sobrenome);
    @Query("SELECT f FROM Funcionario f WHERE TYPE(f) = :tipo")
    <T extends Funcionario> List<T> findAllByTipo(Class<T> tipo);
}
