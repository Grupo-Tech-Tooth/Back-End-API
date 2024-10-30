package com.example.back.service;

import com.example.back.dto.req.AtualizarClienteRequestDto;
import com.example.back.dto.req.SalvarClienteRequestDto;
import com.example.back.dto.res.ClienteResponseDto;
import com.example.back.entity.Cliente;
import com.example.back.entity.LoginInfo;
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

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,
                          PasswordEncoder passwordEncoder,
                          LoginInfoRepository loginInfoRepository) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginInfoRepository = loginInfoRepository;
    }

    public Page<Cliente> listarClientes(Pageable pageable) {
        return clienteRepository.findByLoginInfoDeletadoFalse(pageable); // Exclui clientes deletados
    }

    public Cliente salvarCliente(SalvarClienteRequestDto dto) {
        if (clienteRepository.findByCpf(dto.getCpf()).isPresent()) {
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

    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findByIdAndLoginInfoDeletadoFalse(id);
    }

    public void deletarClientePorId(Long id) {
        Cliente clienteDb = buscarClientePorId(id).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        LoginInfo loginInfo = clienteDb.getLoginInfo();
        loginInfo.setAtivo(false);
        loginInfo.setDeletado(true);
        loginInfo.setDeletadoEm(LocalDateTime.now());

        loginInfoRepository.save(loginInfo);
        clienteRepository.save(clienteDb);
    }

    public ClienteResponseDto atualizarCliente(Long id, AtualizarClienteRequestDto dto) {
        Cliente clienteDb = buscarClientePorId(id).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        // Apenas atualiza campos que podem ser alterados
        clienteDb.setNome(dto.getNome());
        clienteDb.setSobrenome(dto.getSobrenome());
        clienteDb.setDataNascimento(dto.getDataNascimento());
        clienteDb.setGenero(dto.getGenero());

        Cliente clienteAtualizado = clienteRepository.save(clienteDb);
        return new ClienteResponseDto(clienteAtualizado);
    }

    public Optional<Cliente> buscarClientePorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    public List<Cliente> buscarPorNomeOuSobrenome(String nome, String sobrenome) {
        return clienteRepository.findByNomeContainingOrSobrenomeContaining(nome, sobrenome);
    }

    public Optional<Cliente> buscarClientePorEmail(String email) {
        return clienteRepository.findByLoginInfo_Email(email);
    }
}
