package com.example.back.observer;

import com.example.back.dto.req.AgendamentoDTO;

public interface AgendamentoObserver {
    void onAgendamentoCreated(AgendamentoDTO agendamento);
    void onAgendamentoUpdated(AgendamentoDTO agendamento);
    void onAgendamentoDeleted(Long agendamentoId);
    void onAgendamentoRetrieved(AgendamentoDTO agendamento);
}