package com.example.back.config;

import com.example.back.entity.LoginInfo;
import com.example.back.entity.Servico;
import com.example.back.repository.LoginInfoRepository;
import com.example.back.repository.ServicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

// Essa classe ja cria um usuario admin para testes

@Configuration
public class DataLoader implements CommandLineRunner {

    private final LoginInfoRepository loginInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServicoRepository servicoRepository;

    public DataLoader(LoginInfoRepository loginInfoRepository, PasswordEncoder passwordEncoder, ServicoRepository servicoRepository) {
        this.loginInfoRepository = loginInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.servicoRepository = servicoRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("aluno@gmail.com");
        loginInfo.setSenha(passwordEncoder.encode("12345"));

        loginInfoRepository.save(loginInfo);


        Arrays.stream(Servico.Tipo.values())
                .forEach(tipoCarteira -> servicoRepository.save(tipoCarteira.getServico()));
    }

}
