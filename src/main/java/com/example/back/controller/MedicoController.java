package com.example.back.controller;

import com.example.back.dto.req.MedicoRequestDto;
import com.example.back.entity.Medico;
import com.example.back.service.MedicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.back.enums.Hierarquia.MEDICO;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @PostMapping
    public ResponseEntity<Medico> criarMedico(@RequestBody @Valid MedicoRequestDto dto) {

        Medico medico = dto.toMedico();

        medico.setId(null);
        medico.setAtivo(true);
        medico.setDeletado(false);
        medico.setDeletadoEm(null);
        medico.setHierarquia(MEDICO);
        Medico novoMedico = medicoService.salvarMedico(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoMedico);
    }

    @GetMapping
    public ResponseEntity<List<Medico>> listarMedicos() {
        List<Medico> medicos = medicoService.listarMedicos();
        return medicos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(medicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> buscarMedicoPorId(@PathVariable Long id) {
        Optional<Medico> medico = medicoService.buscarMedicoPorId(id);
        return medico.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medico> atualizarMedico(@PathVariable Long id, @RequestBody Medico medicoAtualizado) {
        Optional<Medico> medicoExistente = medicoService.buscarMedicoPorId(id);
        if (medicoExistente.isPresent()) {
            Medico atualizado = medicoService.atualizarMedico(medicoAtualizado);
            return ResponseEntity.ok(atualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMedico(@PathVariable Long id) {
        Optional<Medico> medico = medicoService.buscarMedicoPorId(id);
        if (medico.isPresent()) {
            medicoService.deletarMedico(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nome")
    public ResponseEntity<List<Medico>> buscarPorNomeOuSobrenome(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sobrenome
    ) {

        List<Medico> medicos = medicoService.buscarPorNomeOuSobrenome(nome, sobrenome);

        if (medicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(medicos);
        }

    }

    @GetMapping("/{id}/comissao")
    public ResponseEntity<Double> calcularComissao(
            @PathVariable Long id,
            @RequestParam double valorServico) {

        double comissao = medicoService.calcularComissao(id, valorServico);
        return ResponseEntity.ok(comissao);
    }
}
