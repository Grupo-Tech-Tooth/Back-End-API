package com.example.back.service;

import com.example.back.dto.req.MedicoRequestDto;
import com.example.back.dto.res.MedicoResponseDto;
import com.example.back.entity.Agenda;
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
import com.example.back.entity.Agendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoginInfoRepository loginInfoRepository;
    @Autowired
    private AgendaRepository agendaRepository;

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

    public Medico buscarMedicoPorId(Long id) {
        Optional<Medico> medico = medicoRepository.findByIdAndLoginInfo_DeletadoFalse(id);

        if (medico.isEmpty()) {
            throw new IllegalArgumentException("Médico não encontrado");
        }

        return medico.get();
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

    public List<Medico> buscarPorEmail(String email){
        return medicoRepository.findByLoginInfo_DeletadoFalseAndLoginInfo_EmailContainingIgnoreCase(email);
    }

    public List<Medico> buscarPorCf(String cpf){
        return medicoRepository.findByLoginInfo_DeletadoFalseAndCpfContainingIgnoreCase(cpf);
    }

    public double calcularComissao(Long id, double valorServico) {
        Medico medico = medicoRepository.findByIdAndLoginInfo_DeletadoFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));

        return medico.calcularComissao(valorServico); // Usa o método de calcular comissões da classe Medico
    }

    public List<MedicoResponseDto> filtrarMedicos(String nome, String email, String cpf, String especializacao) {
        return medicoRepository.findAll().stream()
                .filter(medico -> nome == null || medico.getNome().toUpperCase().contains(nome.toUpperCase()) ||
                        (medico.getSobrenome() != null && medico.getSobrenome().toUpperCase().contains(nome.toUpperCase())))
                .filter(medico -> email == null || medico.getLoginInfo().getEmail().toUpperCase().contains(email.toUpperCase()))
                .filter(medico -> cpf == null || medico.getCpf().toUpperCase().contains(cpf.toUpperCase()))
                .filter(medico -> especializacao == null || (medico.getEspecializacao() != null &&
                        medico.getEspecializacao().name().equalsIgnoreCase(especializacao)))
                .map(MedicoResponseDto::converter)
                .toList();
    }

    // Método para obter os dias disponíveis na agenda do médico
    public List<LocalDate> getDiasIndisponiveis(Long medicoId) {
        Agenda agenda = agendaRepository.findByMedicoId(medicoId)
                .orElseThrow(() -> new EntityNotFoundException("Agenda não encontrada para o médico com ID " + medicoId));

        return agenda.getDisponibilidade().stream()
                .map(LocalDateTime::toLocalDate) // Converte para LocalDate
                .distinct() // Remove duplicados
                .sorted()   // Ordena os diasx
                .collect(Collectors.toList());
    }

    // Método para obter os horários disponíveis de um dia específico
    public List<LocalTime> getHorariosIndisponiveis(Long medicoId, LocalDate dia) {
        Agenda agenda = agendaRepository.findByMedicoId(medicoId)
                .orElseThrow(() -> new EntityNotFoundException("Agenda não encontrada para o médico com ID " + medicoId));

       List<LocalTime> diasOcupados = agenda.getDisponibilidade().stream()
                .filter(dataHora -> dataHora.toLocalDate().equals(dia))
                .map(LocalDateTime::toLocalTime)
                .collect(Collectors.toList());

        return diasOcupados;
    }
}
