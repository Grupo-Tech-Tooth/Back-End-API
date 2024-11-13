package com.example.back.service;
import com.example.back.entity.Agendamento;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Scheduled(cron = "0 0 8 * * *", zone = "America/Sao_Paulo")
    public void enviarAvisosUmDiaAntes() {

        log.info("Enviando avisos de consultas do dia seguinte");

        LocalDate dataDeAmanha = LocalDate.now().plusDays(1);

        List<Agendamento> consultasDeAmanha = agendamentoService.buscarPorData(dataDeAmanha);

        for (Agendamento consulta : consultasDeAmanha) {
            String email = consulta.getCliente().getLoginInfo().getEmail();
            try {
                Context context = new Context();
                context.setVariable("nome", consulta.getCliente().getNome());
                context.setVariable("dia", consulta.getDataHora().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                context.setVariable("medico", consulta.getMedico().getNome());


                emailService.sendHtmlEmail(
                        email,
                        "Lembrete: Consulta Hoje",
                        "email-template",
                        context
                );

                System.out.println("E-mail enviado para: " + consulta.getCliente().getLoginInfo().getEmail());
            } catch (MessagingException e) {
                log.error("Erro ao enviar e-mail: " + e.getMessage());
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
                context.setVariable("dia", consulta.getDataHora().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                context.setVariable("medico", consulta.getMedico().getNome());

                emailService.sendHtmlEmail(
                        consulta.getCliente().getLoginInfo().getEmail(),
                        "Lembrete: Consulta Hoje",
                        "email-template",
                        context
                );

            } catch (MessagingException e) {
                log.error("Erro ao enviar e-mail: " + e.getMessage());
            }
        }
    }
}
