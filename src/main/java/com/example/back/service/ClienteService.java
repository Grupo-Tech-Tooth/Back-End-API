package com.example.back.service;

import com.example.back.dto.req.AgendamentoDTO;
import com.example.back.dto.req.AgendamentoMapper;
import com.example.back.dto.req.AtualizarClienteRequestDto;
import com.example.back.dto.req.SalvarClienteRequestDto;
import com.example.back.dto.res.ClienteResponseDto;
import com.example.back.dto.res.FluxoSemanal;
import com.example.back.entity.Agendamento;
import com.example.back.entity.Cliente;
import com.example.back.entity.LoginInfo;
import com.example.back.entity.Medico;
import com.example.back.enums.Hierarquia;
import com.example.back.infra.execption.ResourceNotFoundException;
import com.example.back.repository.ClienteRepository;
import com.example.back.repository.LoginInfoRepository;
import com.example.back.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final LoginInfoRepository loginInfoRepository;
    private final AgendamentoService agendamentoService;
    private final MedicoRepository medicoRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,
                          PasswordEncoder passwordEncoder,
                          LoginInfoRepository loginInfoRepository, AgendamentoService agendamentoService, MedicoRepository medicoRepository) {
        this.clienteRepository = clienteRepository;
        this.loginInfoRepository = loginInfoRepository;
        this.agendamentoService = agendamentoService;
        this.medicoRepository = medicoRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findByLoginInfoDeletadoFalse();
    }

    public Cliente salvarCliente(SalvarClienteRequestDto dto) {
        if (clienteRepository.findByCpfAndLoginInfoDeletadoFalse(dto.getCpf()).isPresent()) {
            throw new IllegalArgumentException("Cliente já existe com esse CPF");
        }

        Medico medico = (Medico) medicoRepository.findByIdAndLoginInfoDeletadoFalse(dto.getMedicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));

        dto.setHierarquia(Hierarquia.CLIENTE);
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setSobrenome(dto.getSobrenome());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setGenero(dto.getGenero());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setCep(dto.getCep());
        cliente.setNumeroResidencia(dto.getNumeroResidencia());
        cliente.setAlergias(dto.getAlergias());
        cliente.setMedicamentos(dto.getMedicamentos());
        cliente.setMedico(medico);

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail(dto.getEmail());
        loginInfo.setCliente(cliente);

        loginInfoRepository.save(loginInfo);
        cliente.setLoginInfo(loginInfo);

        return clienteRepository.save(cliente);
    }

    public Cliente buscarClientePorId(Long id) {
        Optional<Cliente> clienteEncontrado = clienteRepository.findByIdAndLoginInfoDeletadoFalse(id);

        if (clienteEncontrado.isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        return clienteEncontrado.get();
    }

    public void deletarClientePorId(Long id) {
        Cliente clienteDb = buscarClientePorId(id);

        LoginInfo loginInfo = clienteDb.getLoginInfo();
        loginInfo.setAtivo(false);
        loginInfo.setDeletado(true);
        loginInfo.setDeletadoEm(LocalDateTime.now());

        loginInfoRepository.save(loginInfo);
        clienteRepository.save(clienteDb);
    }

    public ClienteResponseDto atualizarCliente(Long id, AtualizarClienteRequestDto dto) {
        Cliente clienteDb = buscarClientePorId(id);

//        Medico medico = (Medico) medicoRepository.findByIdAndLoginInfoDeletadoFalse(dto.getMedicoId())
//                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));

        clienteDb.setNome(dto.getNome());
        clienteDb.setSobrenome(dto.getSobrenome());
        clienteDb.setGenero(dto.getGenero());
        clienteDb.setCep(dto.getCep());
        clienteDb.setCpf(dto.getCpf());
        clienteDb.setDataNascimento(dto.getDataNascimento());
        clienteDb.setNumeroResidencia(dto.getNumeroResidencia());
        clienteDb.setTelefone(dto.getTelefone());
        clienteDb.setAlergias(dto.getAlergias());
        clienteDb.setMedicamentos(dto.getMedicamentos());
        clienteDb.setObservacoes(dto.getObservacoes());
//        clienteDb.setMedico(medico);

        LoginInfo loginInfo = clienteDb.getLoginInfo();
        loginInfo.setEmail(dto.getEmail());

        Cliente clienteAtualizado = clienteRepository.save(clienteDb);
        return new ClienteResponseDto(clienteAtualizado);
    }

    public Optional<Cliente> buscarClientePorCpf(String cpf) {
        Optional<Cliente> cliente = clienteRepository.findByCpfAndLoginInfoDeletadoFalse(cpf);

        if (cliente.isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        return cliente;
    }

    public List<ClienteResponseDto> buscarPorNomeOuSobrenome(String nome, String sobrenome) {

        List<Cliente> clientes = clienteRepository.findByLoginInfoDeletadoFalseAndNomeContainingOrSobrenomeContaining(nome, sobrenome);

        return ClienteResponseDto.converter(clientes);
    }

    public Optional<Cliente> buscarClientePorEmail(String email) {
        Optional<Cliente> cliente = clienteRepository.findByLoginInfoEmailAndLoginInfoDeletadoFalse(email);

        if (cliente.isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        return cliente;
    }

    public Optional<Cliente> buscarClientePorTelefone(String telefone) {
        Optional<Cliente> cliente = clienteRepository.findByTelefoneAndLoginInfoDeletadoFalse(telefone);

        if (cliente.isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        return cliente;
    }

    public List<ClienteResponseDto> buscarClientesComUltimosAgendamentos() {
        List<Cliente> clientesEntidade = clienteRepository.findByLoginInfoDeletadoFalse();

        List<ClienteResponseDto> clientes = ClienteResponseDto.converter(clientesEntidade);

        clientes.forEach(cliente -> {
            if (cliente.getUltimoAgendamento() == null) {
                List<Agendamento> agendamentosEntidade = agendamentoService.buscarAgendamentosPorCliente(cliente.getId());

                List<AgendamentoDTO> agendamentos = AgendamentoMapper.converter(agendamentosEntidade);

                if (!agendamentos.isEmpty()) {
                    int lastIndex = agendamentos.size() - 1;
                    cliente.setUltimoAgendamento(agendamentos.get(lastIndex));
                }
            } else {
                cliente.setUltimoAgendamento(cliente.getUltimoAgendamento());
            }
        });

        return clientes;
    }

    public FluxoSemanal buscarFluxoMensal() {
        // Define o intervalo para o mês atual
        LocalDateTime inicioDoMesDateTime = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime fimDoMesDateTime = inicioDoMesDateTime.plusMonths(1).minusDays(1);
        List<AgendamentoDTO> consultas = agendamentoService.buscarPorPeriodo(inicioDoMesDateTime, fimDoMesDateTime);

        // Mapeia os horários das consultas
        List<LocalDateTime> datas = consultas.stream()
                .map(AgendamentoDTO::dataHora)
                .collect(Collectors.toList());

        // Conta as consultas por dia da semana
        Map<DayOfWeek, Long> consultasPorDiaDaSemana = datas.stream()
                .collect(Collectors.groupingBy(
                        LocalDateTime::getDayOfWeek,
                        Collectors.counting()
                ));

        // Preenche a DTO diretamente, organizando por ordem de domingo a sábado
        return new FluxoSemanal(
                consultasPorDiaDaSemana.getOrDefault(DayOfWeek.SUNDAY, 0L).intValue(),
                consultasPorDiaDaSemana.getOrDefault(DayOfWeek.MONDAY, 0L).intValue(),
                consultasPorDiaDaSemana.getOrDefault(DayOfWeek.TUESDAY, 0L).intValue(),
                consultasPorDiaDaSemana.getOrDefault(DayOfWeek.WEDNESDAY, 0L).intValue(),
                consultasPorDiaDaSemana.getOrDefault(DayOfWeek.THURSDAY, 0L).intValue(),
                consultasPorDiaDaSemana.getOrDefault(DayOfWeek.FRIDAY, 0L).intValue(),
                consultasPorDiaDaSemana.getOrDefault(DayOfWeek.SATURDAY, 0L).intValue()
        );
    }

    public List<ClienteResponseDto> filtrarClientes(String nome, String email, String telefone, String cpf) {
        // Filtragem inicial
        List<Cliente> clientes = clienteRepository.findAll().stream()
                .filter(cliente -> nome == null || cliente.getNome().toUpperCase().contains(nome.toUpperCase()) ||
                        (cliente.getSobrenome() != null && cliente.getSobrenome().toUpperCase().contains(nome.toUpperCase())))
                .filter(cliente -> email == null || cliente.getLoginInfo().getEmail().equalsIgnoreCase(email))
                .filter(cliente -> telefone == null || cliente.getTelefone().equalsIgnoreCase(telefone))
                .filter(cliente -> cpf == null || cliente.getCpf().equals(cpf)) // Filtro por CPF
                .toList();

        // Conversão para DTO
        List<ClienteResponseDto> clientesDto = ClienteResponseDto.converter(clientes);

        // Buscar últimos agendamentos
        clientesDto.forEach(cliente -> {
            List<Agendamento> agendamentosEntidade = agendamentoService.buscarAgendamentosPorCliente(cliente.getId());
            List<AgendamentoDTO> agendamentos = AgendamentoMapper.converter(agendamentosEntidade);

            if (!agendamentos.isEmpty()) {
                int lastIndex = agendamentos.size() - 1;
                cliente.setUltimoAgendamento(agendamentos.get(lastIndex));
            }
        });

        return clientesDto;
    }

}
