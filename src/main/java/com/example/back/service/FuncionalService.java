package com.example.back.service;

import com.example.back.entity.Funcional;
import com.example.back.infra.execption.UsuarioExistenteException;
import com.example.back.repository.FuncionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionalService {

    @Autowired
    private FuncionalRepository funcionalRepository;

    public Funcional salvarFuncional(Funcional funcional) {
        var funcionalDb = funcionalRepository.findByCpf(funcional.getCpf());

        if (funcionalDb.isPresent()) {
            throw new UsuarioExistenteException("Funcional já existe com esse CPF");
        }
        return funcionalRepository.save(funcional);
    }

    public List<Funcional> listarFuncionais() {
        return funcionalRepository.findAll();
    }

    public Optional<Funcional> buscarFuncionalPorId(Long id) {
        return funcionalRepository.findById(id);
    }

    public Funcional atualizarFuncional(Funcional funcional) {
        return funcionalRepository.save(funcional);
    }

    public void deletarFuncional(Long id) {
        funcionalRepository.deleteById(id);
    }

    public List<Funcional> buscarPorNomeOuSobrenome(String nome, String sobrenome) {
        return funcionalRepository.findByNomeContainingOrSobrenomeContainingIgnoreCase(nome, sobrenome);
    }
}
