package com.example.back.repository;

import com.example.back.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByMedicoId(Long medicoId);
    List<Agendamento> findByClienteId(Long clienteId);
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    boolean existsByClienteIdAndDataHoraBetween(Long clienteId, LocalDateTime inicio, LocalDateTime fim);
    boolean existsByMedicoIdAndDataHoraBetween(Long medicoId, LocalDateTime inicio, LocalDateTime fim);
}