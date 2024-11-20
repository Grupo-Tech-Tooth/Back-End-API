package com.example.back.service;

import com.example.back.dto.req.FuncionalRequestDto;
import com.example.back.dto.res.FuncionalResponseDto;
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

    public Funcional atualizarFuncional(Long id, FuncionalRequestDto funcional) {

        Funcional funcionalDb = funcionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcional não encontrado"));

        funcionalDb.setNome(funcional.getNome());
        funcionalDb.setSobrenome(funcional.getSobrenome());
        funcionalDb.setCpf(funcional.getCpf());
        funcionalDb.setDepartamento(funcional.getDepartamento());
        funcionalDb.setDataNascimento(funcional.getDataNascimento());
        funcionalDb.setTelefone(funcional.getTelefone());
        funcionalDb.setGenero(funcional.getGenero());
        funcionalDb.setCep(funcional.getCep());
        funcionalDb.setNumeroResidencia(funcional.getNumeroResidencia());

        LoginInfo loginInfo = funcionalDb.getLoginInfo();
        loginInfo.setEmail(funcional.getEmail());
        loginInfo.setSenha(passwordEncoder.encode(funcional.getSenha()));
        loginInfoRepository.save(loginInfo);

        return funcionalRepository.save(funcionalDb);

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

    public List<Funcional> buscarPorEmail(String email) {
        return funcionalRepository.findByLoginInfo_AtivoTrueAndLoginInfo_EmailContainingIgnoreCase(email);
    }

    public List<Funcional> buscarPorCpf(String cpf) {
        return funcionalRepository.findByLoginInfo_AtivoTrueAndCpfContainingIgnoreCase(cpf);
    }

    public List<Funcional> buscarPorDepartamento(String departamento) {
        return funcionalRepository.findByLoginInfo_AtivoTrueAndDepartamentoContainingIgnoreCase(departamento);
    }

    public List<FuncionalResponseDto> filtrarFuncionais(String nome, String email, String cpf, String departamento) {
        List<Funcional> funcionaisFiltrados = funcionalRepository.findAll().stream()
                .filter(funcional -> nome == null || funcional.getNome().toUpperCase().contains(nome.toUpperCase()) ||
                        (funcional.getSobrenome() != null && funcional.getSobrenome().toUpperCase().contains(nome.toUpperCase())))
                .filter(funcional -> email == null || funcional.getLoginInfo().getEmail().toUpperCase().contains(email.toUpperCase()))
                .filter(funcional -> cpf == null || funcional.getCpf().toUpperCase().contains(cpf.toUpperCase()))
                .filter(funcional -> departamento == null || (funcional.getDepartamento() != null &&
                        funcional.getDepartamento().toUpperCase().contains(departamento.toUpperCase())))
                .toList();

        return FuncionalResponseDto.converter(funcionaisFiltrados); // Usa o metodo estático para listas
    }


}
