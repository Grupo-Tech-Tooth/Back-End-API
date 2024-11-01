package com.example.back.repository;

import com.example.back.entity.Funcional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionalRepository extends JpaRepository<Funcional, Long> {
    Optional<Funcional> findByCpf(String cpf);

    // Mantido método de busca por nome ou sobrenome, mas sem filtro de deletado
    List<Funcional> findByNomeContainingOrSobrenomeContainingIgnoreCase(String nome, String sobrenome);

    // Removido método que busca por CPF considerando deletado, já que o controle está em LoginInfo
    // Optional<Funcional> findByCpfAndDeletadoFalse(String cpf);

    // Removido método que busca por deletado
    // List<Funcional> findByDeletadoFalse();

    // Atualizado método que busca Funcional ativo através de LoginInfo
    Optional<Funcional> findByIdAndLoginInfo_AtivoTrue(Long id);

    // Atualizado método que busca Funcional ativo por nome ou sobrenome
    List<Funcional> findByLoginInfo_AtivoTrueAndNomeContainingOrSobrenomeContainingIgnoreCase(String nome, String sobrenome);

    // Adicionando método para encontrar Funcional ativo
    List<Funcional> findByLoginInfo_AtivoTrue();

    // Adicionando método para encontrar Funcional ativo pelo CPF
    Optional<Funcional> findByCpfAndLoginInfo_AtivoTrue(String cpf);
}
