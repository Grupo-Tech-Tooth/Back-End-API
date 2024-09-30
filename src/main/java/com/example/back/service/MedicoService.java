package com.example.back.service;

import com.example.back.dto.res.MedicoResponseDto;
import com.example.back.entity.Medico;
import com.example.back.infra.execption.UsuarioExistenteException;
import com.example.back.repository.MedicoRepository;
import com.example.back.strategy.Comissao;
import com.example.back.strategy.ComissaoMedico;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Medico salvarMedico(Medico medico) {
        var medicoCpfDb = medicoRepository.findByCpfAndDeletadoFalse(medico.getCpf());
        var medicoEmailDb = medicoRepository.findByEmailAndDeletadoFalse(medico.getEmail());

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
        return medicoRepository.findByDeletadoFalse();
    }

    public Optional<Medico> buscarMedicoPorId(Long id) {
        return medicoRepository.findById(id);
    }

    public Medico atualizarMedico(Medico medico) {
        var medicoIdDb = medicoRepository.findByIdAndDeletadoFalse(medico.getId()).orElseThrow(() -> new IllegalArgumentException("Medico não encontrado"));

        medicoIdDb.setId(medicoRepository.getReferenceById(medico.getId()).getId());
        medicoIdDb.setNome(medico.getNome());
        medicoIdDb.setSobrenome(medico.getSobrenome());
        medicoIdDb.setEmail(medicoRepository.getReferenceById(medico.getId()).getEmail());
        medicoIdDb.setCpf(medicoRepository.getReferenceById(medico.getId()).getCpf());
        medicoIdDb.setSenha(medico.getSenha());
        medicoIdDb.setMatricula(medicoRepository.getReferenceById(medico.getId()).getMatricula());
        medicoIdDb.setCrm(medicoRepository.getReferenceById(medico.getId()).getCrm());
        medicoIdDb.setEspecializacao(medicoRepository.getReferenceById(medico.getId()).getEspecializacao());
        medicoIdDb.setAtivo(medico.getAtivo());

        return medicoRepository.save(medicoIdDb);
    }

    public void deletarMedico(Long id) {
        Medico medicoIdDb = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));
        medicoIdDb.setDeletado(true);
        medicoIdDb.setAtivo(false);
        medicoIdDb.setDeletadoEm(LocalDate.now());
        medicoRepository.save(medicoIdDb);
    }

    public List<Medico> buscarPorNomeOuSobrenome(String nome, String sobrenome) {
        return medicoRepository.findByDeletadoFalseAndNomeContainingOrSobrenomeContainingIgnoreCase(nome, sobrenome);
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

        return new MedicoResponseDto().calcularComissao(valorServico);
    }
}
