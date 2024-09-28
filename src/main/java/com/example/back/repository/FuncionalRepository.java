package com.example.back.repository;

import com.example.back.entity.Funcional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionalRepository extends JpaRepository<Funcional, Long> {
    Optional<Funcional> findByCpf(String cpf);
    List<Funcional> findByNomeContainingOrSobrenomeContainingIgnoreCase(String nome, String sobrenome);

    UserDetails findByEmail(String email);
}