package com.example.back.service;

import com.example.back.entity.Medico;
import com.example.back.infra.execption.UsuarioExistenteException;
import com.example.back.repository.MedicoRepository;
import com.example.back.strategy.Comissao;
import com.example.back.strategy.ComissaoMedico;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Medico salvarMedico(Medico medico) {
        var medicoCpfDb = medicoRepository.findByCpf(medico.getCpf());
        var medicoEmailDb = medicoRepository.findByEmail(medico.getEmail());

        if (medicoCpfDb.isPresent()) {
            throw new UsuarioExistenteException("Médico já existe com esse CPF");
        } else if (medicoEmailDb.isPresent()) {
            throw new UsuarioExistenteException("Médico já existe com esse Email");
        }
        Comissao comissaoStrategy = new ComissaoMedico(5.0); // ou algum outro cálculo

        medico.setComissao(comissaoStrategy); // define a comissão aqui
        medico.setSenha(passwordEncoder.encode(medico.getSenha()));

        return medicoRepository.save(medico);
    }

    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    public Optional<Medico> buscarMedicoPorId(Long id) {
        return medicoRepository.findById(id);
    }

    public Medico atualizarMedico(Medico medico) {
        var medicoIdDb = medicoRepository.findById(medico.getId()).orElseThrow(() -> new IllegalArgumentException("Medico não encontrado"));

        medicoIdDb.setId(medico.getId());
        medicoIdDb.setNome(medico.getNome());
        medicoIdDb.setSobrenome(medico.getSobrenome());
        medicoIdDb.setEmail(medico.getEmail());
        medicoIdDb.setCpf(medico.getCpf());
        medicoIdDb.setSenha(medico.getSenha());
        medicoIdDb.setMatricula(medico.getMatricula());
        medicoIdDb.setCrm(medico.getCrm());
        medicoIdDb.setEspecializacao(medico.getEspecializacao());

        return medicoRepository.save(medico);
    }

    public void deletarMedico(Long id) {
        Medico medicoIdDb = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));
        medicoIdDb.setAtivo(false);
        medicoRepository.save(medicoIdDb);
    }

    public List<Medico> buscarPorNomeOuSobrenome(String nome, String sobrenome) {
        return medicoRepository.findByNomeContainingOrSobrenomeContainingIgnoreCase(nome, sobrenome);
    }

    public double calcularComissao(Long id, double valorServico) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));

        // Inicialize a estratégia de comissão aqui
        if (medico.getComissao() == null) {
            // Exemplo: configuração da comissão com base em um percentual fixo
            // Ou você pode definir uma lógica mais complexa
            medico.setComissao(new ComissaoMedico(5.0)); // Aqui 5.0 é apenas um exemplo
        }

        return medico.calcularComissao(valorServico);
    }
}
