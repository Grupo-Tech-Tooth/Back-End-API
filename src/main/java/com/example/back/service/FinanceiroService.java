package com.example.back.service;

import com.example.back.entity.Financeiro;
import com.example.back.repository.FinanceiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceiroService {

    private final FinanceiroRepository financeiroRepository;

    @Autowired
    public FinanceiroService(FinanceiroRepository financeiroRepository) {
        this.financeiroRepository = financeiroRepository;
    }

    public List<Financeiro> listarFinanceiro(){
        return financeiroRepository();
    }
}
