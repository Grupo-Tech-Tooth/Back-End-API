package com.example.back.service;

import com.example.back.controller.dto.CriarFuncionarioDto;
import com.example.back.entity.Funcionario;
import com.example.back.entity.Usuario;
import com.example.back.exception.UsuarioExistenteException;
import com.example.back.repository.FuncionarioRepository;
import com.example.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public Funcionario salvarFuncionario(CriarFuncionarioDto dto) {

        var funcionarioDb = funcionarioRepository.findByCpf(dto.cpf());

        if (funcionarioDb.isPresent()){
            throw new UsuarioExistenteException("Funcionário já existe com esse CPF");
        }

        Funcionario funcionario = dto.toFuncionario();

        return funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> listarFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> buscarFuncionarioPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    public Funcionario atualizarFuncionario(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public void deletarFuncionario(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public List<Funcionario> buscarPorNomeOuSobrenome(String nome, String sobrenome) {
        return funcionarioRepository.findByNomeContainingOrSobrenomeContaining(nome, sobrenome);
    }

    public Optional<Funcionario> buscarFuncionarioPorEmail(String email) {
        return funcionarioRepository.findByEmail(email);
    }

    public Optional<Funcionario> buscarFuncionarioPorCpf(String cpf) {
        return funcionarioRepository.findByCpf(cpf);
    }
}
