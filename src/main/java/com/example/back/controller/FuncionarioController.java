package com.example.back.controller;

import com.example.back.entity.Funcional;
import com.example.back.entity.Funcionario;
import com.example.back.entity.Medico;
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
    public ResponseEntity<Funcionario> criarFuncionario(
            @RequestBody @Valid Funcionario funcionario,
            @RequestParam String tipoFuncionario) 
    {

        Funcionario novoFuncionario = service.salvarFuncionario(funcionario, tipoFuncionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFuncionario);

    }

    @GetMapping
    public ResponseEntity<List<Funcionario>> listarFuncionarios() {

        List<Funcionario> funcionarios = service.listarFuncionarios();

        if(funcionarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarFuncionarioPorId(@PathVariable Long id) {

        Optional<Funcionario> funcionario = service.buscarFuncionarioPorId(id);
        return funcionario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/medicos/{id}")
    public ResponseEntity<Medico> buscarMedicoPorId(@PathVariable Long id) {

        Optional<Medico> medico = service.buscarMedicoPorId(id);
        return medico.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/funcionais/{id}")
    public ResponseEntity<Funcional> buscarFuncionalPorId(@PathVariable Long id) {

        Optional<Funcional> funcional = service.buscarFuncionalPorId(id);
        return funcional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/medicos/{id}")
    public ResponseEntity<Medico> atualizarMedico(
            @PathVariable Long id, @RequestBody Medico medicoAtualizado
    ) {
        Optional<Medico> medicoExistente = service.buscarMedicoPorId(id);

        if (medicoExistente.isPresent()) {
            Medico atualizado = service.atualizarMedico(medicoAtualizado);
            return ResponseEntity.ok(atualizado);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/funcionais/{id}")
    public ResponseEntity<Funcional> atualizarFuncional(
            @PathVariable Long id, @RequestBody Funcional funcionalAtualizado
    ) {
        Optional<Funcional> funcionalExistente = service.buscarFuncionalPorId(id);

        if (funcionalExistente.isPresent()) {
            Funcional atualizado = service.atualizarFuncional(funcionalAtualizado);
            return ResponseEntity.ok(atualizado);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable Long id) {

        Optional<Funcionario> funcionario = service.buscarFuncionarioPorId(id);

        if (funcionario.isPresent()) {
            service.deletarFuncionario(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/nome")
    public ResponseEntity<List<Funcionario>> buscarPorNomeOuSobrenome(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sobrenome
    ) {

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

    @GetMapping("/medicos")
    public ResponseEntity<List<Medico>> listarMedicos() {

        List<Medico> medicos = service.listarMedicos();
        if(medicos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(medicos);
    }

    @GetMapping("/funcionais")
    public ResponseEntity<List<Funcional>> listarFuncionais() {

        List<Funcional> funcionais = service.listarFuncionais();
        if(funcionais.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(funcionais);

    }
}
