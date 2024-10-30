package com.example.back.service;

import com.example.back.dto.req.FuncionalRequestDto;
import com.example.back.entity.Funcional;
import com.example.back.entity.LoginInfo;
import com.example.back.infra.execption.UsuarioExistenteException;
import com.example.back.repository.FuncionalRepository;
import com.example.back.repository.LoginInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionalService {

    @Autowired
    private FuncionalRepository funcionalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    public Funcional salvarFuncional(FuncionalRequestDto funcionalRequestDto) {
        var funcionalDb = funcionalRepository.findByCpfAndLoginInfo_AtivoTrue(funcionalRequestDto.getCpf());

        if (funcionalDb.isPresent()) {
            throw new UsuarioExistenteException("Funcional já existe com esse CPF");
        }

        Funcional funcional = funcionalRequestDto.toFuncional();  // Converte o DTO para Funcional

        // Criar LoginInfo
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail(funcionalRequestDto.getEmail());
        loginInfo.setSenha(passwordEncoder.encode(funcionalRequestDto.getSenha()));
        loginInfo.setFuncionario(funcional);

        loginInfoRepository.save(loginInfo);

        funcional.setLoginInfo(loginInfo);

        return funcionalRepository.save(funcional);
    }

    public List<Funcional> listarFuncionais() {
        return funcionalRepository.findByLoginInfo_AtivoTrue();
    }

    public Optional<Funcional> buscarFuncionalPorId(Long id) {
        return funcionalRepository.findByIdAndLoginInfo_AtivoTrue(id);
    }

    public Funcional atualizarFuncional(Funcional funcional) {
        return funcionalRepository.save(funcional);
    }

    public void deletarFuncional(Long id) {
        Funcional funcional = funcionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcional não encontrado"));

        // Atualizando o LoginInfo antes de deletar
        LoginInfo loginInfo = funcional.getLoginInfo();
        loginInfo.setDeletado(true);
        loginInfo.setAtivo(false);
        loginInfo.setDeletadoEm(LocalDateTime.now());
        loginInfoRepository.save(loginInfo);

        funcionalRepository.save(funcional);
    }

    public List<Funcional> buscarPorNomeOuSobrenome(String nome, String sobrenome) {
        return funcionalRepository.findByLoginInfo_AtivoTrueAndNomeContainingOrSobrenomeContainingIgnoreCase(nome, sobrenome);
    }
}
