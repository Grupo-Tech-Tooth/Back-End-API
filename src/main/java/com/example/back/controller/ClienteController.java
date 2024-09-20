package com.example.back.controller;


import com.example.back.controller.dto.req.ClienteRequestDto;
import com.example.back.controller.dto.res.ClienteResponseDto;
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

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@SecurityRequirement(name = "bearer-key")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody @Valid Cliente cliente) {
        Cliente novoCliente = service.salvarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    @GetMapping
    public ResponseEntity<Page<Cliente>> listarClientes(Pageable pageable) {

        Page<Cliente> clientes = service.listarClientes(pageable);
        System.out.println(clientes);

        if (clientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
        Optional<Cliente> cliente = service.buscarClientePorId(id);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizarCliente(@PathVariable Long id, @RequestBody @Valid ClienteRequestDto clienteRequestDto) {

        ClienteResponseDto cliente = service.atualizarCliente(id, clienteRequestDto);
        return ResponseEntity.ok(cliente);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarClientePorId(@PathVariable Long id){
        service.deletarClientePorId(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nome")
    public ResponseEntity<List<Cliente>> buscarPorNomeOuSobrenome(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sobrenome) {
            List<Cliente> clientes = service.buscarPorNomeOuSobrenome(nome, sobrenome);

            if (clientes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(clientes);
    }

    @GetMapping("/email")
    public ResponseEntity<Cliente> buscarClientePorEmail(@RequestParam String email){
        Optional<Cliente> cliente = service.buscarClientePorEmail(email);

        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf")
    public ResponseEntity<Cliente> buscarClientePorCpf(@RequestParam String cpf){
        Optional<Cliente> cliente = service.buscarClientePorCpf(cpf);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
