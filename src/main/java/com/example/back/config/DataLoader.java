package com.example.back.config;

import com.example.back.controller.ClienteController;
import com.example.back.dto.req.*;
import com.example.back.dto.res.ClienteResponseDto;
import com.example.back.entity.*;
import com.example.back.enums.FormaPagamento;
import com.example.back.enums.Hierarquia;
import com.example.back.repository.*;
import com.example.back.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

// Essa classe ja cria um usuario admin para testes

@Configuration
public class DataLoader implements CommandLineRunner {

    private final LoginInfoRepository loginInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServicoRepository servicoRepository;
    private final FuncionalRepository funcionalRepository;
    private final ClienteRepository clienteRepository;
    private final MedicoRepository medicoRepository;

    @Autowired
    ClienteService clienteService;

    @Autowired
    MedicoService medicoService;

    @Autowired
    AgendamentoService agendamentoService;

    @Autowired
    FinanceiroService financeiroService;

    @Autowired
    EmailService emailService;

    public DataLoader(LoginInfoRepository loginInfoRepository, PasswordEncoder passwordEncoder, ServicoRepository servicoRepository, FuncionalRepository funcionalRepository, ClienteRepository clienteRepository, MedicoRepository medicoRepository, MedicoRepository medicoRepository1) {
        this.loginInfoRepository = loginInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.servicoRepository = servicoRepository;
        this.funcionalRepository = funcionalRepository;
        this.clienteRepository = clienteRepository;
        this.medicoRepository = medicoRepository1;
    }

    @Override
    public void run(String... args) throws Exception {


        if (!loginInfoRepository.buscarPorEmail("aluno@gmail.com").isPresent()) {
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setEmail("aluno@gmail.com");
            loginInfo.setSenha(passwordEncoder.encode("12345"));
            loginInfoRepository.save(loginInfo);
        }

        if (!loginInfoRepository.buscarPorEmail("yeda@gmail.com").isPresent()) {
            Funcional gerente = new Funcional();
            gerente.setNome("Yeda");
            gerente.setCpf("12345678900");
            gerente.setDepartamento("Gerência");
            gerente.setId(null);

            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setEmail("yeda@gmail.com");
            loginInfo.setSenha(passwordEncoder.encode("123123"));
            loginInfo.setFuncionario(gerente);
            loginInfo.setAtivo(true);

            gerente.setLoginInfo(loginInfo);

            loginInfoRepository.save(loginInfo);
            funcionalRepository.save(gerente);
        }

        if (servicoRepository.count() == 0) {
            Arrays.stream(Servico.Tipo.values())
                    .forEach(tipoCarteira -> servicoRepository.save(tipoCarteira.getServico()));
        }


        // Envio de e-mail para avisar que o sistema foi iniciado
        emailService.sendEmail("grupotech@sptech.school", "Projeto Iniciado", "O Back-end do Projeto acaba de ser iniciado com sucesso");
    }

}
