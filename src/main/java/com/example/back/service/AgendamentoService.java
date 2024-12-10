package com.example.back.service;

import com.example.back.dto.req.AgendamentoDTO;
import com.example.back.dto.req.AgendamentoCreateDTO;
import com.example.back.dto.req.AgendamentoMapper;
import com.example.back.dto.res.AgendamentoResponseDto;
import com.example.back.entity.*;
import com.example.back.infra.execption.BusinessException;
import com.example.back.infra.execption.ResourceNotFoundException;
import com.example.back.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Autowired
    private FilaAgendamentoService filaService;
    @Autowired
    private  PilhaAgendamentoService pilhaAgendamentoService;

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

        List<LocalDateTime> disponibilidade = agenda.getDisponibilidade();
        if (disponibilidade.contains(dto.dataHora())) {
            log.error("Horário indisponível para agendamento");
            throw new BusinessException("Horário indisponível para agendamento");
        }
        disponibilidade.add(dto.dataHora());
        agenda.setDisponibilidade(disponibilidade);
        agendaRepository.save(agenda);

        Agendamento agendamento = AgendamentoMapper.toEntity(dto, cliente, medico, servico, agenda);
        agendamento.setStatus("Pendente");
        agendamento.setDeletado(false);

        // Adiciona o agendamento recém-criado à pilha
        pilhaAgendamentoService.adicionarNaPilha(AgendamentoMapper.toDTO(agendamento));

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
        List<AgendamentoDTO> agendamento = agendamentoRepository.findByClienteId(clienteId).stream()
                .map(AgendamentoMapper::toDTO)
                .collect(Collectors.toList());

        return agendamento;
    }

    public List<Agendamento> buscarPorData(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();

        List<Agendamento> consultas = agendamentoRepository.findByDataHoraBetween(inicio, fim);

        if (consultas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma consulta encontrada para a data: " + data);
        }

        return consultas;
    }

    public List<AgendamentoDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<AgendamentoDTO> consultas = agendamentoRepository.findByDataHoraBetween(inicio, fim).stream()
                .map(AgendamentoMapper::toDTO)
                .collect(Collectors.toList());

        if (consultas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma consulta encontrada para o período: " + inicio + " a " + fim);
        }

        return consultas;
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
        return agendamentoRepository.findByDeletadoFalse().stream()
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
                Cliente cliente = clienteRepository.findById(agendamento.cliente().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
                Medico medico = medicoRepository.findById(agendamento.medico().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));
                Servico servico = servicoRepository.findById(agendamento.servico().getId())
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
        Optional<AgendamentoDTO> consulta = agendamentoRepository.findByClienteIdOrderByDataHoraDesc(id);

        if (consulta.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum agendamento encontrado para o cliente de id: " + id);
        }

        return consulta;
    }

    // Adicionar agendamentos à fila
    public void verificarEAdicionarAgendamentos() {
        LocalDateTime agora = LocalDateTime.now();
        List<Agendamento> agendamentos = agendamentoRepository.findAllByDataHoraBetweenAndStatusIn(
                LocalDateTime.of(agora.toLocalDate(), LocalTime.of(6, 0)),
                LocalDateTime.of(agora.toLocalDate(), LocalTime.of(23, 59)),
                List.of("Pendente")
        );

        for (Agendamento agendamento : agendamentos) {
            if (agendamento.getDataHora().isBefore(agora) || agendamento.getDataHora().isEqual(agora)) {
                filaService.adicionarNaFila(new AgendamentoDTO(
                        agendamento.getId(),
                        agendamento.getCliente(),
                        agendamento.getMedico(),
                        agendamento.getServico(),
                        agendamento.getStatus(),
                        agendamento.getDataHora()
                ));
            }
        }
    }

    // Limpar a fila e concluir agendamentos
    public void limparFilaEConcluirAgendamentos() {
        List<Agendamento> agendamentos = agendamentoRepository.findAllByDataHoraBeforeAndStatusIn(
                LocalDateTime.now(),
                Arrays.asList("Pendente", null)
        );


        for (Agendamento agendamento : agendamentos) {
            agendamento.setStatus("Concluído");
        }

        agendamentoRepository.saveAll(agendamentos);
        filaService.limparFila();
    }

    public AgendamentoDTO concluirConsulta(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        filaService.getFila().removeIf(a -> a.id().equals(id));

        agendamento.setStatus("Concluído");

        return AgendamentoMapper.toDTO(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public AgendamentoDTO desfazerPorId(Long id) {
        AgendamentoDTO agendamentoDTO = pilhaAgendamentoService.desfazerPorId(id);

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));
        agendamento.setStatus("Cancelado");
        agendamento.setCancelado(true);
        return AgendamentoMapper.toDTO(agendamentoRepository.save(agendamento));
    }

    // Metodo para limpar a pilha à meia-noite
    @Scheduled(cron = "0 0 0 * * ?")
    public void limparPilha() {
        pilhaAgendamentoService.limparPilha();
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
        LocalDateTime inicioDoDia = LocalDateTime.now().with(LocalTime.MIN); // Início do dia
        LocalDateTime fimDoDia = LocalDateTime.now().with(LocalTime.MAX);   // Fim do dia

        // Buscar agendamentos do dia
        List<Agendamento> agendamentos = agendamentoRepository.findAgendamentosDoDia(inicioDoDia, fimDoDia);

        // Usar o mapper para converter em DTOs
        return AgendamentoMapper.converter(agendamentos);
    }

    public AgendamentoDTO deletar(Long id) {

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        agendamento.setDeletado(true);
        agendamento.setDeletadoEm(LocalDateTime.now());

        return AgendamentoMapper.toDTO(agendamentoRepository.save(agendamento));

    }
}