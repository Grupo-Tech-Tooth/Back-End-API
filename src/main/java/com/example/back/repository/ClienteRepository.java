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
    Optional<Cliente> findByLoginInfo_Email(String email);
    Optional<Cliente> findByCpf(String cpf);
    List<Cliente> findByNomeContainingOrSobrenomeContaining(String nome, String sobrenome);

    @Query("SELECT c FROM Cliente c WHERE TYPE(c) = :tipo")
    <T extends Cliente> List<T> findAllByTipo(Class<T> tipo);

    Optional<Cliente> findByCpfAndLoginInfoDeletadoFalse(String cpf);
    Page<Cliente> findByLoginInfoDeletadoFalse(Pageable pageable);
    Optional<Cliente> findByIdAndLoginInfoDeletadoFalse(Long id);
    List<Cliente> findByLoginInfoDeletadoFalseAndNomeContainingOrSobrenomeContaining(String nome, String sobrenome);
    Optional<Cliente> findByLoginInfoEmailAndLoginInfoDeletadoFalse(String email);

    // Novo método para buscar clientes ativos
    List<Cliente> findByLoginInfoAtivoTrue();
}
