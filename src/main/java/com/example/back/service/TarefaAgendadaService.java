package com.example.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TarefaAgendadaService {

    @Autowired
    private AgendamentoService agendamentoService;

    // Verificar e adicionar agendamentos a cada minuto
    @Scheduled(cron = "0 * 6-23 * * *") // A cada minuto entre 6:00 e 23:59
    public void adicionarAgendamentosNaFila() {
        agendamentoService.verificarEAdicionarAgendamentos();
    }

    // Limpar fila e concluir agendamentos à meia-noite
    @Scheduled(cron = "0 0 0 * * *") // Todos os dias à meia-noite
    public void limparFilaEDefinirConcluido() {
        agendamentoService.limparFilaEConcluirAgendamentos();
    }
}