package com.example.back.config;

import com.example.back.entity.Funcional;
import com.example.back.entity.LoginInfo;
import com.example.back.entity.Servico;
import com.example.back.enums.Hierarquia;
import com.example.back.repository.FuncionalRepository;
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
    private final FuncionalRepository funcionalRepository;

    public DataLoader(LoginInfoRepository loginInfoRepository, PasswordEncoder passwordEncoder, ServicoRepository servicoRepository, FuncionalRepository funcionalRepository) {
        this.loginInfoRepository = loginInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.servicoRepository = servicoRepository;
        this.funcionalRepository = funcionalRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Verifica se ja existe um usuario admin

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
            loginInfo.setHierarquia(Hierarquia.GERENTE);
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

    }

}
