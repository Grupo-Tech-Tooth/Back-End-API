package com.example.back.service;

import com.example.back.dto.req.AgendamentoDTO;
import com.example.back.dto.req.AgendamentoCreateDTO;
import com.example.back.dto.req.AgendamentoMapper;
import com.example.back.dto.res.AgendamentoResponseDto;
import com.example.back.entity.*;
import com.example.back.infra.execption.BusinessException;
import com.example.back.infra.execption.ResourceNotFoundException;
import com.example.back.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    private static final Logger log = LoggerFactory.getLogger(AgendamentoService.class);

    public AgendamentoDTO criar(AgendamentoCreateDTO dto){
        validarRegrasDeNegocio(dto);

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> {
                    new ResourceNotFoundException("Cliente não encontrado");
                    log.error("Cliente não encontrado");
                    return null;
                });

        Medico medico = dto.medicoId() != null
                ? medicoRepository.findById(dto.medicoId()).orElseThrow(() -> {
                    log.error("Médico não encontrado");
                    throw  new ResourceNotFoundException("Médico não encontrado");
        })
                : escolherMedicoAleatorio(dto.dataHora(), dto);

        Servico servico = servicoRepository.findById(dto.servicoId())
                .orElseThrow(() -> {
                    log.error("Serviço não encontrado");
                    throw new ResourceNotFoundException("Serviço não encontrado");
                });

        Agenda agenda = agendaRepository.findByMedicoId(medico.getId())
                .orElseThrow(() -> {
                    log.error("Agenda não encontrada para o médico");
                    throw new ResourceNotFoundException("Agenda não encontrada para o médico");
                });

        Agendamento agendamento = AgendamentoMapper.toEntity(dto, cliente, medico, servico, agenda);
        agendamento.setStatus("Pendente");

        String mensagem = """
                Olá %s,
                Seu agendamento foi realizado com sucesso.
                Data: %s
                Médico: %s
                Serviço: %s
                """.formatted(cliente.getNome(), agendamento.getDataHora(), medico.getNome(), servico.getNome());

        return AgendamentoMapper.toDTO(agendamentoRepository.save(agendamento));
    }

    private void    validarRegrasDeNegocio(AgendamentoCreateDTO dto) {
        LocalDateTime dataHora = dto.dataHora();
        LocalTime hora = dataHora.toLocalTime();
        DayOfWeek diaSemana = dataHora.getDayOfWeek();

        if (diaSemana == DayOfWeek.SUNDAY || hora.isBefore(LocalTime.of(7, 0)) || hora.isAfter(LocalTime.of(19, 0))) {
            log.error("Horário de funcionamento da clínica é de segunda a sábado, das 07:00 às 19:00");
            throw new BusinessException("Horário de funcionamento da clínica é de segunda a sábado, das 07:00 às 19:00");
        }

        if (hora.isAfter(LocalTime.of(12, 0)) && hora.isBefore(LocalTime.of(13, 0))) {
            log.error("Horário de almoço indisponível para agendamento");
            throw new BusinessException("Horário de almoço indisponível para agendamento");
        }

        if (dataHora.isBefore(LocalDateTime.now().plusMinutes(30))) {
            log.error("As consultas devem ser agendadas com antecedência mínima de 30 minutos");
            throw new BusinessException("As consultas devem ser agendadas com antecedência mínima de 30 minutos");
        }

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        if (!cliente.getLoginInfo().getAtivo()) {
            log.error("Não é permitido agendar consultas para clientes inativos");
            throw new BusinessException("Não é permitido agendar consultas para clientes inativos");
        }

        if (dto.medicoId() != null) {
            Medico medico = medicoRepository.findById(dto.medicoId())
                    .orElseThrow(() -> {
                        log.error("Médico não encontrado");
                        throw new ResourceNotFoundException("Médico não encontrado");
                    });

            if (!medico.getLoginInfo().getAtivo()) {
                log.error("Não é permitido agendar consultas com médicos inativos");
                throw new BusinessException("Não é permitido agendar consultas com médicos inativos");
            }
        }

        Servico servico = servicoRepository.findById(dto.servicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        if (!Objects.equals(dto.status(), "Pendente")) {
            throw new BusinessException("O status precisa estar como presente");
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
        agendamento.setCpf(dto.cpf());
        agendamento.setStatus(dto.status());

        return AgendamentoMapper.toDTO(agendamentoRepository.save(agendamento));
    }

    public AgendamentoDTO buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .map(AgendamentoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));
    }

    public List<AgendamentoDTO> buscarPorMedico(Long medicoId) {
        return agendamentoRepository.findByMedicoId(medicoId).stream()
                .map(AgendamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AgendamentoDTO> buscarPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId).stream()
                .map(AgendamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<Agendamento> buscarPorData(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();
        return agendamentoRepository.findByDataHoraBetween(inicio, fim);
    }

    public List<AgendamentoDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoRepository.findByDataHoraBetween(inicio, fim).stream()
                .map(AgendamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<Servico> listarServicos() { return servicoRepository.findAll(); }

    public AgendamentoDTO cancelarConsulta(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        agendamento.setCancelado(true);
        agendamento.setStatus("Cancelado");
        return AgendamentoMapper.toDTO(agendamentoRepository.save(agendamento));
    }

    public List<AgendamentoDTO> buscarTodosAgendamentos() {
        return agendamentoRepository.findAll().stream()
                .map(AgendamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void gravarArquivoCsv(List<AgendamentoDTO> lista, String nomeArq) {
        FileWriter arq = null;
        Formatter saida = null;
        nomeArq += ".csv";
        try {
            arq = new FileWriter(nomeArq);
            saida = new Formatter(arq);

            saida.format("ID; ClienteNome; ClienteEmail; MedicoNome; DataHora; ServicoNome;\n");

            for (AgendamentoDTO agendamento : lista) {
                Cliente cliente = clienteRepository.findById(agendamento.clienteId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
                Medico medico = medicoRepository.findById(agendamento.medicoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));
                Servico servico = servicoRepository.findById(agendamento.servicoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

                saida.format("%d;%s;%s;%s;%s;%s\n",
                        agendamento.id(),
                        cliente.getNome(),
                        cliente.getLoginInfo().getEmail(),
                        medico.getNome(),
                        agendamento.dataHora().toString(),
                        servico.getNome());
            }
        } catch (IOException | FormatterClosedException | ResourceNotFoundException erro) {
            System.out.println("Erro ao manipular o arquivo");
            erro.printStackTrace();
            throw new RuntimeException("Erro ao manipular o arquivo", erro);
        } finally {
            if (saida != null) {
                saida.close();
            }
            try {
                if (arq != null) {
                    arq.close();
                }
            } catch (IOException erro) {
                System.out.println("Erro ao fechar o arquivo");
                throw new RuntimeException("Erro ao fechar o arquivo", erro);
            }
        }
    }

    public List<Agendamento> buscarAgendamentosPorCliente(Long id) {
        return agendamentoRepository.findAllByClienteIdOrderByDataHoraDesc(id);
    }

    public Optional<AgendamentoDTO> buscarUltimoAgendamentoDeCliente(Long id) {
        return agendamentoRepository.findByClienteIdOrderByDataHoraDesc(id);
    }

    public List<AgendamentoResponseDto> filtrarAgendamentos(String nomeCliente, String nomeServico, String nomeMedico,
                                                            LocalDate dataInicio, LocalDate dataFim) {
        List<Agendamento> agendamentos = agendamentoRepository.findAll().stream()
                .filter(agendamento -> nomeCliente == null ||
                        agendamento.getCliente().getNome().toUpperCase().contains(nomeCliente.toUpperCase()))
                .filter(agendamento -> nomeServico == null ||
                        agendamento.getServico().getNome().toUpperCase().contains(nomeServico.toUpperCase()))
                .filter(agendamento -> nomeMedico == null ||
                        agendamento.getMedico().getNome().toUpperCase().contains(nomeMedico.toUpperCase()))
                .filter(agendamento -> dataInicio == null ||
                        !agendamento.getDataHora().toLocalDate().isBefore(dataInicio))
                .filter(agendamento -> dataFim == null ||
                        !agendamento.getDataHora().toLocalDate().isAfter(dataFim))
                .toList();

        return agendamentos.stream()
                .map(agendamento -> new AgendamentoResponseDto(
                        agendamento.getCliente(),
                        agendamento.getAgenda(),
                        agendamento.getDataHora(),
                        agendamento.getStatus(),
                        agendamento.getCliente().getCpf()
                )).toList();
    }

    public List<AgendamentoDTO> buscarAgendamentosDoDia() {
        return agendamentoRepository.findAgendamentosDoDia().stream()
                .map(AgendamentoMapper::toDTO)
                .collect(Collectors.toList());
    }
}