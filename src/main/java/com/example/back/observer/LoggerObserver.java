package com.example.back.observer;

import com.example.back.dto.req.AgendamentoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerObserver implements AgendamentoObserver {
    private static final Logger logger = LoggerFactory.getLogger(LoggerObserver.class);

    @Override
    public void onAgendamentoCreated(AgendamentoDTO agendamento) {
        logger.info("Agendamento criado com sucesso: ID={}, Cliente={}, Médico={}, Data={}, Horário={}",
                agendamento.id(), agendamento.clienteId(), agendamento.medicoId(),
                agendamento.dataHora().toLocalDate(), agendamento.dataHora().toLocalTime());
    }

    @Override
    public void onAgendamentoUpdated(AgendamentoDTO agendamento) {
        logger.info("Agendamento atualizado: ID={}, Cliente={}, Médico={}, Nova Data={}, Novo Horário={}",
                agendamento.id(), agendamento.clienteId(), agendamento.medicoId(),
                agendamento.dataHora().toLocalDate(), agendamento.dataHora().toLocalTime());
    }

    @Override
    public void onAgendamentoDeleted(Long agendamentoId) {
        logger.info("Agendamento com ID={} foi deletado", agendamentoId);
    }

    @Override
    public void onAgendamentoRetrieved(AgendamentoDTO agendamento) {
        logger.info("Agendamento consultado: ID={}, Cliente={}, Médico={}, Data={}, Horário={}",
                agendamento.id(), agendamento.clienteId(), agendamento.medicoId(),
                agendamento.dataHora().toLocalDate(), agendamento.dataHora().toLocalTime());
    }

    public void logNotFoundException(Long id) {
        logger.warn("Tentativa de acesso a agendamento com ID={} falhou. Recurso não encontrado (404).", id);
    }

    public void logBusinessException(String mensagem) {
        logger.error("Erro de negócio: {}", mensagem);
    }

    public void logUnexpectedError(Exception e) {
        logger.error("Erro inesperado: {}", e.getMessage(), e);
    }
}