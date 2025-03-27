package com.example.back.repository;

import com.example.back.dto.req.AgendamentoDTO;
import com.example.back.entity.Agendamento;
import com.example.back.entity.Medico;
import org.hibernate.dialect.function.TruncFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByDeletadoFalse();
    List<Agendamento> findByMedicoId(Long medicoId);
    List<Agendamento> findByClienteId(Long clienteId);
    // buscar por id do cliente em ordem decrescente, do mais recente para o mais antigo
    List<Agendamento> findAllByClienteIdOrderByDataHoraDesc(Long clienteId);
    Optional<AgendamentoDTO> findByClienteIdOrderByDataHoraDesc(Long clienteId);
    @Query("SELECT a FROM Agendamento a WHERE a.dataHora BETWEEN :inicio AND :fim AND a.status != 'Cancelado' AND a.deletado = false")
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    boolean existsByClienteIdAndDataHoraBetween(Long clienteId, LocalDateTime inicio, LocalDateTime fim);
    boolean existsByMedicoIdAndDataHoraBetween(Long medicoId, LocalDateTime inicio, LocalDateTime fim);
    boolean existsByIdAndDataHoraBefore(Long id, LocalDateTime dateTime);
    //Buscas pro uso de Fila
    List<Agendamento> findAllByDataHoraBetweenAndStatusIn(LocalDateTime inicio, LocalDateTime fim, List<String> status);
    List<Agendamento> findAllByDataHoraBeforeAndStatusIn(LocalDateTime dataHora, List<String> status);

    @Query("SELECT a FROM Agendamento a WHERE a.dataHora BETWEEN :inicio AND :fim")
    List<Agendamento> findAgendamentosDoDia(LocalDateTime inicio, LocalDateTime fim);
    List<Agendamento> findByMedicoAndDataHoraBetween(Medico medico, LocalDateTime inicio, LocalDateTime fim);
    boolean existsByMedicoAndDataHora(Medico medico, LocalDateTime dataHora);
}