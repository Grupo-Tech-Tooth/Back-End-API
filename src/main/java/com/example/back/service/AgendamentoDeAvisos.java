package com.example.back.service;
import com.example.back.entity.Agendamento;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AgendamentoDeAvisos {

    private static final Logger log = LoggerFactory.getLogger(AgendamentoDeAvisos.class);

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private EmailService emailService;

    // Executa diariamente às 8h para avisar sobre consultas de amanhã
    @Scheduled(cron = "0 0 8 * * *", zone = "America/Sao_Paulo")
    public void enviarAvisosUmDiaAntes() {

        log.info("Enviando avisos de consultas do dia seguinte");

        LocalDate dataDeAmanha = LocalDate.now().plusDays(1);

        // Obter consultas do dia seguinte
        List<Agendamento> consultasDeAmanha = agendamentoService.buscarPorData(dataDeAmanha);

        // Enviar e-mail para cada cliente
        for (Agendamento consulta : consultasDeAmanha) {
            String email = consulta.getCliente().getLoginInfo().getEmail();
            String nomeCliente = consulta.getCliente().getNome();
            String mensagem = String.format(
                    "Olá %s, lembramos que você tem uma consulta agendada para amanhã às %s.",
                    nomeCliente, consulta.getDataHora().toLocalTime()
            );

            try {
                Context context = new Context();
                context.setVariable("nome", consulta.getCliente().getNome());
                context.setVariable("dia", consulta.getDataHora().toLocalDate());
                context.setVariable("medico", consulta.getMedico());


                emailService.sendHtmlEmail(
                        "toretomarcos50@gmail.com",
                        "Lembrete: Consulta Hoje",
                        "email",
                        context
                );

                System.out.println("E-mail enviado para: " + consulta.getCliente().getLoginInfo().getEmail());
            } catch (MessagingException e) {
                System.err.println("Erro ao enviar e-mail: " + e.getMessage());
            }
        }
    }

    @Scheduled(cron = "0 30 17 * * *", zone = "America/Sao_Paulo")
    public void enviarAvisosNoDia() {
        System.out.println("Executando tarefa de envio de avisos no dia");

        LocalDate dataDeHoje = LocalDate.now();
        List<Agendamento> consultasDeHoje = agendamentoService.buscarPorData(dataDeHoje);

        for (Agendamento consulta : consultasDeHoje) {
            try {
                Context context = new Context();
                context.setVariable("nome", consulta.getCliente().getNome());
                context.setVariable("dia", consulta.getDataHora().toLocalDate());
                context.setVariable("medico", consulta.getMedico());

                emailService.sendHtmlEmail(
                        "toretomarcos50@gmail.com",
                        "Lembrete: Consulta Hoje",
                        "email",
                        context
                );

                System.out.println("E-mail enviado para: " + consulta.getCliente().getLoginInfo().getEmail());
            } catch (MessagingException e) {
                System.err.println("Erro ao enviar e-mail: " + e.getMessage());
            }
        }
    }
}
