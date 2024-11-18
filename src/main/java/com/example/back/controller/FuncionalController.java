package com.example.back.controller;

import com.example.back.dto.req.FuncionalRequestDto;
import com.example.back.entity.Funcional;
import com.example.back.entity.Medico;
import com.example.back.service.FuncionalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.back.enums.Hierarquia.FUNCIONAL;

@RestController
@RequestMapping("/funcionais")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "http://localhost:3000")
public class FuncionalController {

    @Autowired
    private FuncionalService funcionalService;

    @PostMapping
    public ResponseEntity<Funcional> criarFuncional(@RequestBody @Valid FuncionalRequestDto dto) {

        Funcional novoFuncional = funcionalService.salvarFuncional(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFuncional);
    }

    @GetMapping
    public ResponseEntity<List<Funcional>> listarFuncionais() {
        List<Funcional> funcionais = funcionalService.listarFuncionais();
        return funcionais.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(funcionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcional> buscarFuncionalPorId(@PathVariable Long id) {
        Optional<Funcional> funcional = funcionalService.buscarFuncionalPorId(id);
        return funcional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcional> atualizarFuncional(@PathVariable Long id, @RequestBody FuncionalRequestDto funcionalAtualizado) {

        Funcional funcional = funcionalService.atualizarFuncional(id, funcionalAtualizado);
        return ResponseEntity.ok(funcional);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFuncional(@PathVariable Long id) {
        Optional<Funcional> funcional = funcionalService.buscarFuncionalPorId(id);
        if (funcional.isPresent()) {
            funcionalService.deletarFuncional(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nome")
    public ResponseEntity<List<Funcional>> buscarPorNomeOuSobrenome(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sobrenome
    ) {

        List<Funcional> funcionais = funcionalService.buscarPorNomeOuSobrenome(nome, sobrenome);

        if (funcionais.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(funcionais);
        }

    }

    @GetMapping("/email")
    public ResponseEntity<List<Funcional>> buscarPorEmail(
            @RequestParam String email
    ){

        List<Funcional> funcionals = funcionalService.buscarPorEmail(email);

        if (funcionals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok(funcionals);
        }
    }

    @GetMapping("/cpf")
    public ResponseEntity<List<Funcional>> buscarPorCpf(
            @RequestParam String cpf
    ){

        List<Funcional> funcionals = funcionalService.buscarPorCpf(cpf);

        if (funcionals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok(funcionals);
        }
    }

    @GetMapping("/departamento")
    public ResponseEntity<List<Funcional>> buscarPorDepartamento(
            @RequestParam String departamento
    ){

        List<Funcional> funcionals = funcionalService.buscarPorDepartamento(departamento);

        if (funcionals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok(funcionals);
        }
    }

}
