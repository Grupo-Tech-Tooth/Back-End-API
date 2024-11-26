package com.example.back.repository;

import com.example.back.entity.Financeiro;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FinanceiroRepository {

    Date findByDataPagamento(Date dataPagamento);
    String findByNome(String nome);
    String findByCpf(String cpf);
    String findByMetodoPagamento(String metodoPagamento);
}
