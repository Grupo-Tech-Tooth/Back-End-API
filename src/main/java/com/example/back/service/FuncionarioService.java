package com.example.back.service;

import com.example.back.entity.Funcional;
import com.example.back.entity.Funcionario;
import com.example.back.entity.Medico;
import com.example.back.exception.UsuarioExistenteException;
import com.example.back.repository.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public Funcionario salvarFuncionario(Funcionario funcionarioCadastrar, String tipoFuncionario) {

        var funcionarioDb = funcionarioRepository.findByCpf(funcionarioCadastrar.getCpf());

        if (funcionarioDb.isPresent()) {
            throw new UsuarioExistenteException("Funcionário já existe com esse CPF");
        }

        // Verificar o tipo de funcionário e criar a instância correta
        Funcionario novoFuncionario;
        if ("medico".equalsIgnoreCase(tipoFuncionario)) {
            novoFuncionario = new Medico(); // Configurar especializações, comissões, etc.
            ((Medico) novoFuncionario).setEspecializacao(((Medico) funcionarioCadastrar).getEspecializacao());
        } else if ("funcional".equalsIgnoreCase(tipoFuncionario)) {
            novoFuncionario = new Funcional();
        } else {
            throw new IllegalArgumentException("Tipo de funcionário inválido.");
        }

        // Copiar os atributos comuns
        novoFuncionario.setNome(funcionarioCadastrar.getNome());
        novoFuncionario.setSobrenome(funcionarioCadastrar.getSobrenome());
        novoFuncionario.setEmail(funcionarioCadastrar.getEmail());
        novoFuncionario.setCpf(funcionarioCadastrar.getCpf());
        novoFuncionario.setSenha(funcionarioCadastrar.getSenha());

        return funcionarioRepository.save(novoFuncionario);
    }

    public List<Funcionario> listarFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> buscarFuncionarioPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    public Optional<Medico> buscarMedicoPorId(Long id) {
        return funcionarioRepository.findById(id)
                .filter(Medico.class::isInstance)
                .map(Medico.class::cast);
    }

    public Optional<Funcional> buscarFuncionalPorId(Long id) {
        return funcionarioRepository.findById(id)
                .filter(Funcional.class::isInstance)
                .map(Funcional.class::cast);
    }

    public Medico atualizarMedico(Medico medicoAtualizado) {
        Medico medicoExistente = (Medico) buscarFuncionarioPorId(medicoAtualizado.getId())
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));
        medicoExistente.setEspecializacao(medicoAtualizado.getEspecializacao());
        // Outros campos específicos de Medico...
        return funcionarioRepository.save(medicoExistente);
    }

    public Funcional atualizarFuncional(Funcional funcionalAtualizado) {
        Funcional funcionalExistente = (Funcional) buscarFuncionarioPorId(funcionalAtualizado.getId())
                .orElseThrow(() -> new EntityNotFoundException("Funcional não encontrado"));
        // Atualizar campos específicos de Funcional...
        return funcionarioRepository.save(funcionalExistente);
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

    public List<Medico> listarMedicos() {
        return funcionarioRepository.findAllByTipo(Medico.class);
    }

    public List<Funcional> listarFuncionais() {
        return funcionarioRepository.findAllByTipo(Funcional.class);
    }
}
