package com.example.back.service;

import com.example.back.dto.req.MedicoRequestDto;
import com.example.back.entity.LoginInfo;
import com.example.back.entity.Medico;
import com.example.back.infra.execption.UsuarioExistenteException;
import com.example.back.repository.AgendaRepository;
import com.example.back.repository.LoginInfoRepository;
import com.example.back.repository.MedicoRepository;
import com.example.back.strategy.Comissao;
import com.example.back.strategy.ComissaoMedico;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoginInfoRepository loginInfoRepository;

    @Transactional
    public Medico salvarMedico(MedicoRequestDto medicoDto) {
        var medicoCpfDb = medicoRepository.findByLoginInfoFuncionarioCpfAndLoginInfo_DeletadoFalse(medicoDto.getCpf());
        var medicoEmailDb = medicoRepository.findByLoginInfoEmailAndLoginInfo_DeletadoFalse(medicoDto.getEmail());

        if (medicoCpfDb.isPresent()) {
            throw new UsuarioExistenteException("Médico já existe com esse CPF");
        } else if (medicoEmailDb.isPresent()) {
            throw new UsuarioExistenteException("Médico já existe com esse Email");
        }

        Comissao comissaoStrategy = new ComissaoMedico(5.0); // ou algum outro cálculo

        // Criando o médico a partir do DTO
        Medico medico = medicoDto.toMedico(); // Usa o método toMedico do DTO
        medico.setComissao(comissaoStrategy); // define a comissão aqui

        // Criando e salvando LoginInfo
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail(medicoDto.getEmail());
        loginInfo.setSenha(passwordEncoder.encode(medicoDto.getSenha()));
        loginInfo.setFuncionario(medico);

        loginInfoRepository.save(loginInfo);
        medico.setLoginInfo(loginInfo);

        return medicoRepository.save(medico);
    }

    public List<Medico> listarMedicos() {
        return medicoRepository.findByLoginInfo_DeletadoFalse();
    }

    public Optional<Medico> buscarMedicoPorId(Long id) {
        return medicoRepository.findByIdAndLoginInfo_DeletadoFalse(id);
    }

    public Medico atualizarMedico(Long id, MedicoRequestDto medicoRequestDto) {
        Medico medicoIdDb = medicoRepository.findByIdAndLoginInfo_DeletadoFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));

        medicoIdDb.setNome(medicoRequestDto.getNome());
        medicoIdDb.setSobrenome(medicoRequestDto.getSobrenome());
        medicoIdDb.setCpf(medicoRequestDto.getCpf());
        medicoIdDb.setCrm(medicoRequestDto.getCrm());
        medicoIdDb.setEspecializacao(medicoRequestDto.getEspecializacao());

        LoginInfo loginInfo = medicoIdDb.getLoginInfo();
        loginInfo.setEmail(medicoRequestDto.getEmail());

        if (medicoRequestDto.getSenha() != null) {
            loginInfo.setSenha(passwordEncoder.encode(medicoRequestDto.getSenha()));
        }

        return medicoRepository.save(medicoIdDb);
    }

    public void deletarMedico(Long id) {
        Medico medicoIdDb = medicoRepository.findByIdAndLoginInfo_DeletadoFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));

        // Agora tratando o LoginInfo relacionado
        LoginInfo loginInfo = medicoIdDb.getLoginInfo();
        if (loginInfo != null) {
            loginInfo.setDeletado(true);
            loginInfo.setAtivo(false);
            loginInfo.setDeletadoEm(LocalDateTime.now());

            loginInfoRepository.save(loginInfo);
        }

        medicoRepository.save(medicoIdDb);
    }

    public List<Medico> buscarPorNomeOuSobrenome(String nome, String sobrenome) {
        return medicoRepository.findByLoginInfo_DeletadoFalseAndNomeContainingOrSobrenomeContainingIgnoreCase(nome, sobrenome);
    }

    public double calcularComissao(Long id, double valorServico) {
        Medico medico = medicoRepository.findByIdAndLoginInfo_DeletadoFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));

        return medico.calcularComissao(valorServico); // Usa o método de calcular comissões da classe Medico
    }
}
