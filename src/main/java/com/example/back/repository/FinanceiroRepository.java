package com.example.back.repository;

import com.example.back.entity.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {
    List<Financeiro> findByAndDeletadoFalse();
    Optional<Financeiro> findByIdAndDeletadoFalse(Long id);

}
