package com.example.back.controller;


import com.example.back.dto.req.AtualizarClienteRequestDto;
import com.example.back.dto.req.SalvarClienteRequestDto;
import com.example.back.dto.res.ClienteResponseDto;
import com.example.back.entity.Cliente;
import com.example.back.service.ClienteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.back.enums.Hierarquia.CLIENTE;

@RestController
@RequestMapping("/clientes")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "http://localhost:3000")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody @Valid SalvarClienteRequestDto cliente) {
        Cliente novoCliente = service.salvarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarClientes() {

        List<Cliente> clientes = service.listarClientes();

        List<ClienteResponseDto> clientesDto = ClienteResponseDto.converter(clientes);

        if (clientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(clientesDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarClientePorId(@PathVariable Long id) {
        Cliente cliente = service.buscarClientePorId(id);
        ClienteResponseDto clienteResponseDto = new ClienteResponseDto(cliente);
        return ResponseEntity.ok(clienteResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizarCliente(@PathVariable Long id, @RequestBody @Valid AtualizarClienteRequestDto clienteRequestDto) {

        ClienteResponseDto cliente = service.atualizarCliente(id, clienteRequestDto);
        return ResponseEntity.ok(cliente);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarClientePorId(@PathVariable Long id){
        service.deletarClientePorId(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nome")
    public ResponseEntity<List<ClienteResponseDto>> buscarPorNomeOuSobrenome(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sobrenome) {
            List<ClienteResponseDto> clientes = service.buscarPorNomeOuSobrenome(nome, sobrenome);

            if (clientes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(clientes);
    }

    @GetMapping("/email")
    public ResponseEntity<ClienteResponseDto> buscarClientePorEmail(@RequestParam String email){
        Optional<Cliente> cliente = service.buscarClientePorEmail(email);

        return cliente.map(ClienteResponseDto::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf")
    public ResponseEntity<ClienteResponseDto> buscarClientePorCpf(@RequestParam String cpf){
        Optional<Cliente> cliente = service.buscarClientePorCpf(cpf);

        return cliente.map(ClienteResponseDto::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
