package com.example.back.repository;

import com.example.back.dto.req.AgendamentoDTO;
import com.example.back.entity.Agendamento;
import org.hibernate.dialect.function.TruncFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByMedicoId(Long medicoId);
    List<Agendamento> findByClienteId(Long clienteId);
    // buscar por id do cliente em ordem decrescente, do mais recente para o mais antigo
    List<Agendamento> findAllByClienteIdOrderByDataHoraDesc(Long clienteId);
    Optional<AgendamentoDTO> findByClienteIdOrderByDataHoraDesc(Long clienteId);
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    boolean existsByClienteIdAndDataHoraBetween(Long clienteId, LocalDateTime inicio, LocalDateTime fim);
    boolean existsByMedicoIdAndDataHoraBetween(Long medicoId, LocalDateTime inicio, LocalDateTime fim);
    boolean existsByIdAndDataHoraBefore(Long id, LocalDateTime dateTime);

    //Buscas pro uso de Fila
    List<Agendamento> findAllByDataHoraBetweenAndStatus(LocalDateTime inicio, LocalDateTime fim, List<String> status);
    List<Agendamento> findAllByDataHoraBeforeAndStatus(LocalDateTime fim, List<String> status);
}