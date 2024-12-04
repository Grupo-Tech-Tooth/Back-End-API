package com.example.back.controller;

import com.example.back.dto.req.FinanceiroDtoRequest;
import com.example.back.dto.res.FinanceiroResponseDto;
import com.example.back.entity.Financeiro;
import com.example.back.service.FinanceiroService;
import com.example.back.service.FuncionalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/financeiro")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "*")
public class FinanceiroController {

    @Autowired
    private FinanceiroService financeiroService;

    @PostMapping
    public ResponseEntity<Financeiro> criarFinanceiro(@Valid @RequestBody FinanceiroDtoRequest dto) {
        Financeiro novoFinanceiro = financeiroService.criarFinanceiro(dto);
        return ResponseEntity.status(200).body(novoFinanceiro);
    }

    @GetMapping
    public ResponseEntity<List<Financeiro>> listarFinanceiros() {
        List<Financeiro> financeiros = financeiroService.listarFinanceiros();
        if (financeiros.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(financeiros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Financeiro> atualizarFinanceiro(@PathVariable Long id, @Valid @RequestBody FinanceiroDtoRequest dto) {
        Financeiro financeiro = financeiroService.atualizarFinanceiro(id, dto);
        return ResponseEntity.status(200).body(financeiro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Financeiro> deletarFinanceiro(@PathVariable Long id) {
            financeiroService.deletarFinanceiro(id);
            return ResponseEntity.status(204).build();
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<FinanceiroResponseDto>> filtrarFinanceiro(
            @RequestParam(required = false) String nomePaciente,
            @RequestParam(required = false) LocalDate dataPagamento,
            @RequestParam(required = false) String metodoPagamento){
        List<FinanceiroResponseDto> financas = financeiroService.filtrarFinancas(nomePaciente, dataPagamento, metodoPagamento);
        if (financas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(financas);
    }


}
