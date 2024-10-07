package com.example.back.repository;


import com.example.back.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    Optional<Cliente> findByCpfAndDeletadoFalse(String cpf);

    Page<Cliente> findByDeletadoFalse(Pageable pageable);


    Optional<Cliente> findByIdAndDeletadoFalse(Long id);

    List<Cliente> findByDeletadoFalseAndNomeContainingOrSobrenomeContaining(String nome, String sobrenome);

    Optional<Cliente> findByEmailAndDeletadoFalse(String email);
}
