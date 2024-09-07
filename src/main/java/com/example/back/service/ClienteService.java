package com.example.back.service;

import com.example.back.entity.Cliente;
import com.example.back.repository.ClienteRepository;
import feign.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente salvarCliente(Cliente cliente) {
        var clienteDb = clienteRepository.findByCpf(cliente.getCpf());

        if (clienteDb.isPresent()) {
            throw new IllegalArgumentException("Cliente já existe com esse CPF");
        }

        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public void deletarClientePorId(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente atualizarCliente(Cliente cliente) {
        var clienteDb = clienteRepository.findById(cliente.getId()).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        clienteDb.setNome(cliente.getNome());
        clienteDb.setSobrenome(cliente.getSobrenome());
        clienteDb.setEmail(cliente.getEmail());
        clienteDb.setCpf(cliente.getCpf());
        clienteDb.setSenha(cliente.getSenha());

        return clienteRepository.save(clienteDb);
    }

    public Optional<Cliente> buscarClientePorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    public List<Cliente> buscarPorNomeOuSobrenome(String nome, String sobrenome) {
        return clienteRepository.findByNomeContainingOrSobrenomeContaining(nome, sobrenome);
    }

    public Optional<Cliente> buscarClientePorEmail(String email){
        return clienteRepository.findByEmail(email);
    }

}




