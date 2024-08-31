package com.example.back.controller;

import com.example.back.controller.dto.CriarFuncionarioDto;
import com.example.back.controller.dto.FuncionarioDto;
import com.example.back.entity.Funcionario;
import com.example.back.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService service;

    @PostMapping
    public ResponseEntity<Funcionario> criarFuncionario(@RequestBody @Valid CriarFuncionarioDto dto) {
        Funcionario novoFuncionario = service.salvarFuncionario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFuncionario);
    }

    @GetMapping
    public ResponseEntity<List<Funcionario>> listarFuncionarios() {
        List<Funcionario> funcionarios = service.listarFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarFuncionarioPorId(@PathVariable Long id) {
        Optional<Funcionario> funcionario = service.buscarFuncionarioPorId(id);
        return funcionario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizarFuncionario(@PathVariable Long id, @RequestBody Funcionario funcionario) {
        Optional<Funcionario> funcionarioExistente = service.buscarFuncionarioPorId(id);
        if (funcionarioExistente.isPresent()) {
            Funcionario atualizado = service.atualizarFuncionario(funcionario);
            return ResponseEntity.ok(atualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable Long id) {
        service.deletarFuncionario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nome")
    public ResponseEntity<List<Funcionario>> buscarPorNomeOuSobrenome(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sobrenome) {
        List<Funcionario> funcionarios = service.buscarPorNomeOuSobrenome(nome, sobrenome);
        if (funcionarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(funcionarios);
        }
    }

    @GetMapping("/email")
    public ResponseEntity<Funcionario> buscarFuncionarioPorEmail(@RequestParam String email) {
        Optional<Funcionario> funcionario = service.buscarFuncionarioPorEmail(email);
        return funcionario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf")
    public ResponseEntity<Funcionario> buscarFuncionarioPorCpf(@RequestParam String cpf) {
        Optional<Funcionario> funcionario = service.buscarFuncionarioPorCpf(cpf);
        return funcionario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
