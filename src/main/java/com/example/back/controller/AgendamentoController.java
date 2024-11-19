package com.example.back.controller;

import com.example.back.dto.req.AgendamentoCreateDTO;
import com.example.back.dto.req.AgendamentoDTO;
import com.example.back.dto.req.AgendamentoMapper;
import com.example.back.entity.Servico;
import com.example.back.observer.LoggerObserver;
import com.example.back.service.AgendamentoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.InputStreamResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agendamentos")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;
    @Autowired
    private LoggerObserver loggerObserver;

    @PostMapping
    public ResponseEntity<AgendamentoDTO> criar(@RequestBody @Valid AgendamentoCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoDTO>> buscarTodos() {
        List<AgendamentoDTO> agendamentos = agendamentoService.buscarTodosAgendamentos();

        if (agendamentos.isEmpty()) {
            loggerObserver.logBusinessException("Nenhum agendamento encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(agendamentos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AgendamentoCreateDTO dto) {
        return ResponseEntity.ok(agendamentoService.atualizar(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<AgendamentoDTO>> buscarPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(agendamentoService.buscarPorMedico(medicoId));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AgendamentoDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(agendamentoService.buscarPorCliente(clienteId));
    }

    @GetMapping("/cliente/agendamento/{clienteId}")
    public ResponseEntity<Optional<AgendamentoDTO>> buscarUltimoAgendamentoDeCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(agendamentoService.buscarUltimoAgendamentoDeCliente(clienteId));
    }

    @GetMapping("/data")
    public ResponseEntity<List<AgendamentoDTO>> buscarPorData(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(agendamentoService.buscarPorData(data).stream()
                .map(AgendamentoMapper::toDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<AgendamentoDTO>> buscarPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {

        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A data de início deve ser anterior à data de fim");
        }

        if (inicio.isEqual(fim)) {
            throw new IllegalArgumentException("A data de início não pode ser igual à data de fim");
        }

        List<AgendamentoDTO> agendamentos = agendamentoService.buscarPorPeriodo(inicio, fim);

        if (agendamentos.isEmpty()) {
            loggerObserver.logBusinessException("Nenhum agendamento encontrado para o período: " + inicio + " a " + fim);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(agendamentos);
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoDTO> cancelarConsulta(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.cancelarConsulta(id));
    }

    @GetMapping("/exportar-csv")
    public ResponseEntity<InputStreamResource> exportarCsv() {
        List<AgendamentoDTO> agendamentos = agendamentoService.buscarTodosAgendamentos();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        agendamentoService.gravarArquivoCsv(agendamentos, "agendamentos");

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=agendamentos.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/servicos")
    public ResponseEntity<List<Servico>> listarServicos() {
        return ResponseEntity.ok(agendamentoService.listarServicos());
    }
}