package com.example.back.repository;

import com.example.back.entity.Financeiro;
import com.example.back.enums.EspecializacaoOdontologica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {
    List<Financeiro> findByAndDeletadoFalse();
    List<Financeiro> findAllByDeletadoFalse();
    Optional<Financeiro> findByIdAndDeletadoFalse(Long id);
    List<Financeiro> findByMedicoEspecializacaoAndDeletadoFalse(EspecializacaoOdontologica especializacao);
    @Query("SELECT f FROM Financeiro f WHERE f.deletado = false AND f.dataPagamento BETWEEN :inicio AND :fim")
    List<Financeiro> findByDataPagamentoBetweenAndDeletadoFalse(LocalDateTime inicio, LocalDateTime fim);
    @Query("SELECT f FROM Financeiro f WHERE f.cliente.id = :clienteId AND f.deletado = false")
    List<Financeiro> findByClienteIdAndDeletadoFalse(Long clienteId);
}
