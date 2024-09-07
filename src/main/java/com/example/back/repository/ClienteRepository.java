package com.example.back.repository;


import com.example.back.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByCpf(String cpf);
    List<Cliente> findByNomeContainingOrSobrenomeContaining(String nome, String sobrenome);
    @Query("SELECT c FROM Cliente c WHERE TYPE(c) = :tipo")
    <T extends Cliente> List<T> findAllByTipo(Class<T> tipo);


}
