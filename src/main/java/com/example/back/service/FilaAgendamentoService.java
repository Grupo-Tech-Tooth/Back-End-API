package com.example.back.service;

import com.example.back.dto.req.AgendamentoDTO;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class FilaAgendamentoService {

    private final Queue<AgendamentoDTO> fila = new ConcurrentLinkedQueue<>();

    public void adicionarNaFila(AgendamentoDTO agendamento) {
        fila.add(agendamento);
    }

    public void limparFila() {
        fila.clear();
    }

    public Queue<AgendamentoDTO> getFila() {
        return fila;
    }
}