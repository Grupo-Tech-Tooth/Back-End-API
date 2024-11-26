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

    Optional<Cliente> findByCpfAndLoginInfoDeletadoFalse(String cpf);
    List<Cliente> findByLoginInfoDeletadoFalse();
    Optional<Cliente> findByIdAndLoginInfoDeletadoFalse(Long id);
    List<Cliente> findByLoginInfoDeletadoFalseAndNomeContainingOrSobrenomeContaining(String nome, String sobrenome);
    Optional<Cliente> findByLoginInfoEmailAndLoginInfoDeletadoFalse(String email);
    Optional<Cliente> findByTelefoneAndLoginInfoDeletadoFalse(String telefone);

}
