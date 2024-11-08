package com.example.back.service;
import com.example.back.entity.Agendamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AgendamentoDeAvisos {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private EmailService emailService;

    // Executa diariamente às 8h para avisar sobre consultas de amanhã
    @Scheduled(cron = "0 0 8 * * *")
    public void enviarAvisosUmDiaAntes() {
        LocalDateTime dataDeAmanha = LocalDateTime.now().plusDays(1);

        // Obter consultas do dia seguinte
        List<Agendamento> consultasDeAmanha = agendamentoService.obterConsultasPorData(dataDeAmanha);

        // Enviar e-mail para cada cliente
        for (Agendamento consulta : consultasDeAmanha) {
            String email = consulta.getCliente().getLoginInfo().getEmail();
            String nomeCliente = consulta.getCliente().getNome();
            String mensagem = String.format(
                    "Olá %s, lembramos que você tem uma consulta agendada para amanhã às %s.",
                    nomeCliente, consulta.getDataHora().toLocalTime()
            );

            emailService.sendEmail("toretomarcos50@gmail.com", "Lembrete: Consulta Amanhã", mensagem);
        }
    }

    // Executa diariamente às 7h para avisar sobre consultas do dia
    @Scheduled(cron = "0 0 7 * * *")
    public void enviarAvisosNoDia() {
        LocalDateTime dataDeHoje = LocalDateTime.now();

        // Obter consultas do dia
        List<Agendamento> consultasDeHoje = agendamentoService.obterConsultasPorData(dataDeHoje);

        // Enviar e-mail para cada cliente
        for (Agendamento consulta : consultasDeHoje) {
            String email = consulta.getCliente().getLoginInfo().getEmail();
            String nomeCliente = consulta.getCliente().getNome();
            String mensagem = String.format(
                    "Bom dia %s, este é um lembrete de que você tem uma consulta agendada hoje às %s.",
                    nomeCliente, consulta.getDataHora().toLocalTime()
            );

            emailService.sendEmail("toretomarcos50@gmail.com", "Lembrete: Consulta Hoje", mensagem);
        }
    }
}
