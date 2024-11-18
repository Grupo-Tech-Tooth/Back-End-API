package com.example.back.controller;

import com.example.back.dto.req.ServicoDtoRequest;
import com.example.back.dto.res.ServicoDTO;
import com.example.back.entity.Servico;
import com.example.back.repository.ServicoRepository;
import com.example.back.service.ServicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public ResponseEntity<List<Servico>> listarServicos() {
        List<Servico> servicos = servicoRepository.findAll();

        if (servicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(servicos);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Servico> cadastrarServico(@RequestBody ServicoDtoRequest servicoDtoRequest) {
        Servico servico = servicoService.cadastrarServico(servicoDtoRequest);
        return ResponseEntity.ok(servico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizarServico(@PathVariable Long id, @RequestBody ServicoDtoRequest servicoDtoRequest) {
        Servico servico = servicoService.atualizarServico(id, servicoDtoRequest);
        return ResponseEntity.ok(servico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        servicoService.deletarServico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usados")
    public List<ServicoDTO> buscarServicosMaisUsados(@RequestParam("periodo") String periodo) {
        if (periodo.equalsIgnoreCase("mensal")) {
            return servicoService.buscarMaisUsadosMensal();
        } else if (periodo.equalsIgnoreCase("anual")) {
            return servicoService.buscarMaisUsadosAnual();
        } else {
            throw new IllegalArgumentException("Período inválido. Use 'mensal' ou 'anual'.");
        }
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<ServicoDtoRequest>> filtrarServicos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer duracao,
            @RequestParam(required = false) BigDecimal preco) {
        List<ServicoDtoRequest> servicos = servicoService.filtrarServicos(nome, duracao, preco);
        return ResponseEntity.ok(servicos);
    }

}
