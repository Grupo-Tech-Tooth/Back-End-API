package com.example.back.service;

import com.example.back.controller.dto.AgendamentoDTO;
import com.example.back.controller.dto.AgendamentoCreateDTO;
import com.example.back.controller.dto.AgendamentoMapper;
import com.example.back.entity.*;
import com.example.back.infra.execption.BusinessException;
import com.example.back.infra.execption.ResourceNotFoundException;
import com.example.back.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private AgendamentoMapper agendamentoMapper;

    public AgendamentoDTO criar(AgendamentoCreateDTO dto) {
        validarRegrasDeNegocio(dto);

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Medico medico = dto.medicoId() != null
                ? medicoRepository.findById(dto.medicoId()).orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"))
                : escolherMedicoAleatorio(dto.dataHora(), dto);

        Servico servico = servicoRepository.findById(dto.servicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        Agenda agenda = agendaRepository.findByMedicoId(medico.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Agenda não encontrada para o médico"));

        Agendamento agendamento = agendamentoMapper.toEntity(dto, cliente, medico, servico, agenda);
        return agendamentoMapper.toDTO(agendamentoRepository.save(agendamento));
    }

    private void validarRegrasDeNegocio(AgendamentoCreateDTO dto) {
        LocalDateTime dataHora = dto.dataHora();
        LocalTime hora = dataHora.toLocalTime();
        DayOfWeek diaSemana = dataHora.getDayOfWeek();

        if (diaSemana == DayOfWeek.SUNDAY || hora.isBefore(LocalTime.of(7, 0)) || hora.isAfter(LocalTime.of(19, 0))) {
            throw new BusinessException("Horário de funcionamento da clínica é de segunda a sábado, das 07:00 às 19:00");
        }

        if (hora.isAfter(LocalTime.of(12, 0)) && hora.isBefore(LocalTime.of(13, 0))) {
            throw new BusinessException("Horário de almoço indisponível para agendamento");
        }

        if (dataHora.isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new BusinessException("As consultas devem ser agendadas com antecedência mínima de 30 minutos");
        }

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        if (!cliente.getAtivo()) {
            throw new BusinessException("Não é permitido agendar consultas para clientes inativos");
        }

        if (dto.medicoId() != null) {
            Medico medico = medicoRepository.findById(dto.medicoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));
            if (!medico.getAtivo()) {
                throw new BusinessException("Não é permitido agendar consultas com médicos inativos");
            }
        }

        LocalDateTime inicioDia = dataHora.toLocalDate().atStartOfDay();
        LocalDateTime fimDia = inicioDia.plusDays(1);
        if (agendamentoRepository.existsByClienteIdAndDataHoraBetween(dto.clienteId(), inicioDia, fimDia)) {
            throw new BusinessException("Não é permitido agendar mais de uma consulta no mesmo dia para um mesmo cliente");
        }

        Servico servico = servicoRepository.findById(dto.servicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));
        LocalDateTime fimConsulta = dataHora.plusMinutes(servico.getDuracaoMinutos());

        if (dto.medicoId() != null && agendamentoRepository.existsByMedicoIdAndDataHoraBetween(dto.medicoId(), dataHora, fimConsulta)) {
            throw new BusinessException("O médico já possui outra consulta agendada neste horário");
        }
    }

    private Medico escolherMedicoAleatorio(LocalDateTime dataHora, AgendamentoCreateDTO dto) {
        Servico servico = servicoRepository.findById(dto.servicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));
        LocalDateTime fimConsulta = dataHora.plusMinutes(servico.getDuracaoMinutos());

        List<Medico> medicosDisponiveis = medicoRepository.findAvailableMedicos(dataHora, fimConsulta);
        if (medicosDisponiveis.isEmpty()) {
            throw new BusinessException("Não há médicos disponíveis para o horário solicitado");
        }
        return medicosDisponiveis.get(new Random().nextInt(medicosDisponiveis.size()));
    }

    public AgendamentoDTO atualizar(Long id, AgendamentoCreateDTO dto) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        validarRegrasDeNegocio(dto);

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Medico medico = dto.medicoId() != null
                ? medicoRepository.findById(dto.medicoId()).orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"))
                : escolherMedicoAleatorio(dto.dataHora(), dto);

        Servico servico = servicoRepository.findById(dto.servicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        Agenda agenda = agendaRepository.findByMedicoId(medico.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Agenda não encontrada para o médico"));

        agendamento.setCliente(cliente);
        agendamento.setMedico(medico);
        agendamento.setServico(servico);
        agendamento.setAgenda(agenda);
        agendamento.setDataHora(dto.dataHora());

        return agendamentoMapper.toDTO(agendamentoRepository.save(agendamento));
    }

    public void deletar(Long id) {
        if (!agendamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agendamento não encontrado");
        }
        agendamentoRepository.deleteById(id);
    }

    public AgendamentoDTO buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .map(agendamentoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));
    }

    public List<AgendamentoDTO> buscarPorMedico(Long medicoId) {
        return agendamentoRepository.findByMedicoId(medicoId).stream()
                .map(agendamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AgendamentoDTO> buscarPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId).stream()
                .map(agendamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AgendamentoDTO> buscarPorData(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();
        return agendamentoRepository.findByDataHoraBetween(inicio, fim).stream()
                .map(agendamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AgendamentoDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoRepository.findByDataHoraBetween(inicio, fim).stream()
                .map(agendamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AgendamentoDTO cancelarConsulta(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        if (agendamentoRepository.existsByIdAndDataHoraBefore(id, LocalDateTime.now().plusHours(24))) {
            throw new BusinessException("Não é permitido cancelar consultas com menos de 24 horas de antecedência");
        }

        agendamento.setCancelado(true);
        return agendamentoMapper.toDTO(agendamentoRepository.save(agendamento));
    }
}