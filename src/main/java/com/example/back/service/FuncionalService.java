package com.example.back.service;

import com.example.back.entity.Funcional;
import com.example.back.entity.LoginInfo;
import com.example.back.entity.Medico;
import com.example.back.infra.execption.UsuarioExistenteException;
import com.example.back.repository.FuncionalRepository;
import com.example.back.repository.LoginInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionalService {

    @Autowired
    private FuncionalRepository funcionalRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Funcional salvarFuncional(Funcional funcional) {
        var funcionalDb = funcionalRepository.findByCpfAndDeletadoFalse(funcional.getCpf());

        if (funcionalDb.isPresent()) {
            throw new UsuarioExistenteException("Funcional já existe com esse CPF");
        }

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail(funcional.getEmail());
        loginInfo.setSenha(passwordEncoder.encode(funcional.getSenha()));
        loginInfo.setFuncionario(funcional);

        funcional.setLoginInfo(loginInfo);

        funcional.setSenha(passwordEncoder.encode(funcional.getSenha()));

        return funcionalRepository.save(funcional);
    }

    public List<Funcional> listarFuncionais() {
        return funcionalRepository.findByDeletadoFalse();
    }

    public Optional<Funcional> buscarFuncionalPorId(Long id) {
        return funcionalRepository.findByIdAndDeletadoFalse(id);
    }

    public Funcional atualizarFuncional(Funcional funcional) {
        return funcionalRepository.save(funcional);
    }

    public void deletarFuncional(Long id) {
        Funcional funcional = funcionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcional não encontrado"));
        funcional.setDeletado(true);
        funcional.setAtivo(false);
        funcional.setDeletadoEm(LocalDate.now());
        funcionalRepository.save(funcional);
    }

    public List<Funcional> buscarPorNomeOuSobrenome(String nome, String sobrenome) {
        return funcionalRepository.findByDeletadoFalseAndNomeContainingOrSobrenomeContainingIgnoreCase(nome, sobrenome);
    }
}
