package com.example.back.service;

import com.example.back.dto.req.AtualizarClienteRequestDto;
import com.example.back.dto.req.SalvarClienteRequestDto;
import com.example.back.dto.res.ClienteResponseDto;
import com.example.back.entity.Agendamento;
import com.example.back.entity.Cliente;
import com.example.back.entity.LoginInfo;
import com.example.back.infra.execption.ResourceNotFoundException;
import com.example.back.repository.ClienteRepository;
import com.example.back.repository.LoginInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginInfoRepository loginInfoRepository;
    private final AgendamentoService agendamentoService;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,
                          PasswordEncoder passwordEncoder,
                          LoginInfoRepository loginInfoRepository, AgendamentoService agendamentoService) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginInfoRepository = loginInfoRepository;
        this.agendamentoService = agendamentoService;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findByLoginInfoDeletadoFalse();
    }

    public Cliente salvarCliente(SalvarClienteRequestDto dto) {
        if (clienteRepository.findByCpfAndLoginInfoDeletadoFalse(dto.getCpf()).isPresent()) {
            throw new IllegalArgumentException("Cliente já existe com esse CPF");
        }

        Cliente cliente = new Cliente(dto);

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail(dto.getEmail());
        loginInfo.setSenha(passwordEncoder.encode(dto.getSenha()));
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

        clienteDb.setNome(dto.getNome());
        clienteDb.setSobrenome(dto.getSobrenome());
        clienteDb.setDataNascimento(dto.getDataNascimento());
        clienteDb.setGenero(dto.getGenero());

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

    public List<ClienteResponseDto> buscarClientesComUltimosAgendamentos() {
        List<Cliente> clientesEntidade = clienteRepository.findByLoginInfoDeletadoFalse();

        List<ClienteResponseDto> clientes = ClienteResponseDto.converter(clientesEntidade);

        clientes.forEach(cliente -> {
            List<Agendamento> agendamentos = agendamentoService.buscarAgendamentosPorCliente(cliente.getId());
            if (!agendamentos.isEmpty()) {
                agendamentos.sort((a1, a2) -> a2.getDataHora().compareTo(a1.getDataHora()));
                cliente.setUltimoAgendamento(agendamentos.get(0));
            }
        });

        return clientes;
    }
}
