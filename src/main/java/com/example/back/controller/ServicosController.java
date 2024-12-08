package com.example.back.controller;

import com.example.back.dto.req.ServicoDtoRequest;
import com.example.back.dto.res.ServicoDTO;
import com.example.back.entity.Servico;
import com.example.back.repository.ServicoRepository;
import com.example.back.service.ServicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/servicos")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "http://localhost:3000")
public class ServicosController {

    private final ServicoService servicoService;

    @Autowired
    public ServicosController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @GetMapping
    public ResponseEntity<List<Servico>> listarServicos() {
        List<Servico> servicos = servicoService.listarServicos();

        if (servicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(servicos);
    }

    @PostMapping
    public ResponseEntity<Servico> cadastrarServico(@Valid @RequestBody ServicoDtoRequest servicoDtoRequest) {
        Servico servico = servicoService.cadastrarServico(servicoDtoRequest);
        return ResponseEntity.ok(servico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizarServico(@PathVariable Long id, @Valid @RequestBody ServicoDtoRequest servicoDtoRequest) {
        Servico servico = servicoService.atualizarServico(id, servicoDtoRequest);
        return ResponseEntity.ok(servico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        servicoService.deletarServico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usados")
    public ResponseEntity<List<ServicoDTO>> buscarServicosMaisUsados(@RequestParam("periodo") String periodo) {
        List<ServicoDTO> servicos;

        if (periodo.equalsIgnoreCase("mensal")) {
            servicos = servicoService.buscarMaisUsadosMensal();
        } else if (periodo.equalsIgnoreCase("anual")) {
            servicos = servicoService.buscarMaisUsadosAnual();
        } else {
            throw new IllegalArgumentException("Período inválido. Use 'mensal' ou 'anual'.");
        }

        if(servicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<ServicoDtoRequest>> filtrarServicos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer duracao,
            @RequestParam(required = false) BigDecimal preco,
            @RequestParam(required = false) String descricao) {

        if (duracao != null && duracao <= 0) {
            throw new IllegalArgumentException("A duração deve ser maior que zero.");
        }

        if (preco != null && preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }

        List<ServicoDtoRequest> servicos = servicoService.filtrarServicos(nome, duracao, preco, descricao);
        return ResponseEntity.ok(servicos);
    }

}
