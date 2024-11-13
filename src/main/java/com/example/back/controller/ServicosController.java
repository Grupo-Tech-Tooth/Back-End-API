package com.example.back.controller;

import com.example.back.dto.res.ServicoDTO;
import com.example.back.entity.Servico;
import com.example.back.repository.ServicoRepository;
import com.example.back.service.ServicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
