package com.example.back.service;

import com.example.back.dto.req.AgendamentoDTO;
import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
public class PilhaAgendamentoService {

    private final Stack<AgendamentoDTO> pilha = new Stack<>();

    // Adiciona um agendamento na pilha
    public void adicionarNaPilha(AgendamentoDTO agendamento) {
        pilha.push(agendamento);
    }

    // Desfazer um agendamento específico pelo ID
    public AgendamentoDTO desfazerPorId(Long id) {
        for (AgendamentoDTO agendamento : pilha) {
            if (agendamento.id().equals(id)) {
                pilha.remove(agendamento);  // Remove o agendamento da pilha
                return agendamento;  // Retorna o agendamento para uso no serviço
            }
        }
        throw new IllegalStateException("Agendamento não encontrado na pilha para desfazer.");
    }

    // Limpar a pilha à meia-noite
    public void limparPilha() {
        pilha.clear();
        System.out.println("Pilha de agendamentos limpa à meia-noite.");
    }

    // Retorna a pilha atual
    public Stack<AgendamentoDTO> getPilha() {
        return pilha;
    }
}