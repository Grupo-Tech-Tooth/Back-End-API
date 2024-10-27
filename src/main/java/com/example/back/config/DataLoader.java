package com.example.back.config;

import com.example.back.entity.LoginInfo;
import com.example.back.repository.LoginInfoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

// Essa classe ja cria um usuario admin para testes

@Configuration
public class DataLoader implements CommandLineRunner {

    private final LoginInfoRepository loginInfoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(LoginInfoRepository loginInfoRepository, PasswordEncoder passwordEncoder) {
        this.loginInfoRepository = loginInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("aluno@gmail.com");
        loginInfo.setSenha(passwordEncoder.encode("12345"));

        loginInfoRepository.save(loginInfo);

    }

}
