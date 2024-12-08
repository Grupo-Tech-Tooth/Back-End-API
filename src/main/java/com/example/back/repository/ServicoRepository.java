package com.example.back.repository;

import com.example.back.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByNomeContainingIgnoreCase(String nome);

    List<Servico> findByDuracaoMinutos(Integer duracao);

    List<Servico> findByPreco(BigDecimal bigDecimal);

    //Buscar os não deletados (deletado false)
    List<Servico> findByDeletadoFalse();
}
