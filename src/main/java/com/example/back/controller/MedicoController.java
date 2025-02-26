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

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "*")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @PostMapping
    public ResponseEntity<MedicoResponseDto> criarMedico(@RequestBody @Valid MedicoRequestDto dto) {
        Medico novoMedico = medicoService.salvarMedico(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(MedicoResponseDto.converter(novoMedico));
    }

    @GetMapping
    public ResponseEntity<List<MedicoResponseDto>> listarMedicos() {
        List<Medico> medicos = medicoService.listarMedicos();
        return medicos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(MedicoResponseDto.converter(medicos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDto> buscarMedicoPorId(@PathVariable Long id) {
        Medico medico = medicoService.buscarMedicoPorId(id);

        return ResponseEntity.ok(MedicoResponseDto.converter(medico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponseDto> atualizarMedico(@PathVariable Long id, @RequestBody MedicoRequestDto medicoAtualizado) {
        Medico atualizado = medicoService.atualizarMedico(id, medicoAtualizado);
        return ResponseEntity.ok(MedicoResponseDto.converter(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMedico(@PathVariable Long id) {
        medicoService.deletarMedico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nome")
    public ResponseEntity<List<MedicoResponseDto>> buscarPorNomeOuSobrenome(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sobrenome
    ) {

        List<Medico> medicos = medicoService.buscarPorNomeOuSobrenome(nome, sobrenome);

        if (medicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(MedicoResponseDto.converter(medicos));
        }
    }

    @GetMapping("/email")
    public ResponseEntity<List<MedicoResponseDto>> buscarPorEmail(
            @RequestParam String email
    ){

        List<Medico> medicos = medicoService.buscarPorEmail(email);

        if (medicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok(MedicoResponseDto.converter(medicos));
        }
    }

    @GetMapping("/cpf")
    public ResponseEntity<List<MedicoResponseDto>> buscarPorCpf(
            @RequestParam String cpf
    ){

        List<Medico> medicos = medicoService.buscarPorCpf(cpf);

        if (medicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok(MedicoResponseDto.converter(medicos));
        }
    }

    @GetMapping("/{id}/comissao")
    public ResponseEntity<Double> calcularComissao(
            @PathVariable Long id,
            @RequestParam double valorServico) {

        double comissao = medicoService.calcularComissao(id, valorServico);
        return ResponseEntity.ok(comissao);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<MedicoResponseDto>> filtrarMedicos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String crm,
            @RequestParam(required = false) String especializacao) {

        List<MedicoResponseDto> medicos = medicoService.filtrarMedicos(nome, email, crm, especializacao);

        if (medicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(medicos);
    }

    @GetMapping("/{medicoId}/agenda/dias-indisponiveis")
    public ResponseEntity<DiasDisponiveisResponse> getDiasDisponiveis(@PathVariable Long medicoId) {
        List<LocalDate> diasDisponiveis = medicoService.getDiasIndisponiveis(medicoId);

        if (diasDisponiveis.isEmpty()) {
            return ResponseEntity.ok(new DiasDisponiveisResponse(diasDisponiveis));
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{medicoId}/agenda/horarios-indisponiveis")
    public ResponseEntity<HorariosDisponiveisResponse> getHorariosDisponiveis(
            @PathVariable Long medicoId,
            @RequestParam("dia") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dia) {
        List<LocalTime> horariosDisponiveis = medicoService.getHorariosIndisponiveis(medicoId, dia);

        if (horariosDisponiveis.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new HorariosDisponiveisResponse(horariosDisponiveis));
    }

    @GetMapping("/cpf/identification")
    public ResponseEntity<Optional<Long>> buscarIdDoMedicoPorCpf(@RequestParam String cpf) {
        Optional<Long> medicoId = medicoService.buscarIdDoMedicoPorCpf(cpf);

        return ResponseEntity.ok(medicoId);
    }
}
