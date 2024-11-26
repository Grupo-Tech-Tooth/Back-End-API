package com.example.back.controller;

import com.example.back.dto.req.MedicoRequestDto;
import com.example.back.dto.res.DiasDisponiveisResponse;
import com.example.back.dto.res.HorariosDisponiveisResponse;
import com.example.back.dto.res.MedicoResponseDto;
import com.example.back.entity.Medico;
import com.example.back.service.MedicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.example.back.enums.Hierarquia.MEDICO;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "*")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @PostMapping
    public ResponseEntity<Medico> criarMedico(@RequestBody @Valid MedicoRequestDto dto) {
        Medico novoMedico = medicoService.salvarMedico(dto);
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
    public ResponseEntity<Medico> atualizarMedico(@PathVariable Long id, @RequestBody MedicoRequestDto medicoAtualizado) {
        Optional<Medico> medicoExistente = medicoService.buscarMedicoPorId(id);

        if (medicoExistente.isPresent()) {
            Medico atualizado = medicoService.atualizarMedico(id, medicoAtualizado);
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

    @GetMapping("/email")
    public ResponseEntity<List<Medico>> buscarPorEmail(
            @RequestParam String email
    ){

        List<Medico> medicos = medicoService.buscarPorEmail(email);

        if (medicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok(medicos);
        }
    }

    @GetMapping("/cpf")
    public ResponseEntity<List<Medico>> buscarPorCpf(
            @RequestParam String cpf
    ){

        List<Medico> medicos = medicoService.buscarPorCf(cpf);

        if (medicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else {
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

    @GetMapping("/medicos/filtrar")
    public ResponseEntity<List<MedicoResponseDto>> filtrarMedicos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String especializacao) {

        List<MedicoResponseDto> medicos = medicoService.filtrarMedicos(nome, email, cpf, especializacao);
        return ResponseEntity.ok(medicos);
    }

    @GetMapping("/{medicoId}/agenda/dias-disponiveis")
    public ResponseEntity<DiasDisponiveisResponse> getDiasDisponiveis(@PathVariable Long medicoId) {
        List<LocalDate> diasDisponiveis = medicoService.getDiasDisponiveis(medicoId);
        return ResponseEntity.ok(new DiasDisponiveisResponse(diasDisponiveis));
    }

    @GetMapping("/{medicoId}/agenda/horarios-disponiveis")
    public ResponseEntity<HorariosDisponiveisResponse> getHorariosDisponiveis(
            @PathVariable Long medicoId,
            @RequestParam("dia") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dia) {
        List<LocalTime> horariosDisponiveis = medicoService.getHorariosDisponiveis(medicoId, dia);
        return ResponseEntity.ok(new HorariosDisponiveisResponse(horariosDisponiveis));
    }
}
