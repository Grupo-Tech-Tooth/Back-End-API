package com.example.back.repository;

import com.example.back.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    Optional<Agenda> findByMedicoId(Long id);
}
