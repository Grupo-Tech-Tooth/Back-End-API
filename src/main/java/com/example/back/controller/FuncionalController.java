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
public class FuncionalController {

    @Autowired
    private FuncionalService funcionalService;

    @PostMapping
    public ResponseEntity<Funcional> criarFuncional(@RequestBody @Valid FuncionalRequestDto dto) {

        Funcional funcional = dto.toFuncional();

        funcional.setId(null);
        funcional.setAtivo(true);
        funcional.setDeletado(false);
        funcional.setDeletadoEm(null);
        funcional.setHierarquia(FUNCIONAL);
        Funcional novoFuncional = funcionalService.salvarFuncional(funcional);
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
    public ResponseEntity<Funcional> atualizarFuncional(@PathVariable Long id, @RequestBody Funcional funcionalAtualizado) {
        Optional<Funcional> funcionalExistente = funcionalService.buscarFuncionalPorId(id);
        if (funcionalExistente.isPresent()) {
            Funcional atualizado = funcionalService.atualizarFuncional(funcionalAtualizado);
            return ResponseEntity.ok(atualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
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
}
