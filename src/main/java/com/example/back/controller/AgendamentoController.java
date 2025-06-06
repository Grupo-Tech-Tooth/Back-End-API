package com.example.back.controller;

import com.example.back.dto.req.AgendamentoCreateDTO;
import com.example.back.dto.req.AgendamentoDTO;
import com.example.back.dto.req.AgendamentoMapper;
import com.example.back.dto.res.AgendamentoResponseDto;
import com.example.back.entity.Servico;
import com.example.back.observer.LoggerObserver;
import com.example.back.service.AgendamentoService;
import com.example.back.service.FilaAgendamentoService;
import com.example.back.service.PilhaAgendamentoService;
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
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/agendamentos")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;
    @Autowired
    private FilaAgendamentoService filaService;
    @Autowired
    private PilhaAgendamentoService pilhaAgendamentoService;
    @Autowired
    private LoggerObserver loggerObserver;

    @PostMapping
    public ResponseEntity<AgendamentoDTO> criar(@RequestBody @Valid AgendamentoCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criar(dto));
    }

    @PostMapping("/encaixe")
    public ResponseEntity<AgendamentoDTO> encaixe(@RequestBody @Valid AgendamentoCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.encaixe(dto));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoDTO>> buscarTodos() {
        List<AgendamentoDTO> agendamentos = agendamentoService.buscarTodosAgendamentos();

        if (agendamentos.isEmpty()) {
            loggerObserver.logBusinessException("Nenhum agendamento encontrado");
            return ResponseEntity.noContent().build();
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

        List<AgendamentoDTO> agendamentos = agendamentoService.buscarPorCliente(clienteId);

        if (agendamentos.isEmpty()) {
            loggerObserver.logBusinessException("Nenhum agendamento encontrado para o cliente de id: " + clienteId);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(agendamentos);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> deletar(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.deletar(id));
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

        List<Servico> consultas = agendamentoService.listarServicos();

        if (consultas.isEmpty()) {
            loggerObserver.logBusinessException("Nenhum serviço encontrado");
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/fila")
    public Queue<AgendamentoDTO> obterFila() {
        return filaService.getFila();
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<AgendamentoDTO> concluirConsulta(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.concluirConsulta(id));
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<AgendamentoResponseDto>> filtrarAgendamentos(
            @RequestParam(required = false) String nomeCliente,
            @RequestParam(required = false) String nomeServico,
            @RequestParam(required = false) String nomeMedico,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<AgendamentoResponseDto> agendamentos = agendamentoService.filtrarAgendamentos(
                nomeCliente, nomeServico, nomeMedico, dataInicio, dataFim);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/hoje")
    public ResponseEntity<List<AgendamentoDTO>> buscarAgendamentosDoDia() {
        List<AgendamentoDTO> agendamentos = agendamentoService.buscarAgendamentosDoDia();

        if (agendamentos.isEmpty()) {
            loggerObserver.logBusinessException("Nenhum agendamento encontrado para o dia de hoje");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(agendamentos);
    }

    @DeleteMapping("/desfazer/{id}")
    public ResponseEntity<AgendamentoDTO> desfazerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.desfazerPorId(id));
    }

    @GetMapping("/pilha")
    public ResponseEntity<Stack<AgendamentoDTO>> visualizarPilha() {
        return ResponseEntity.ok(pilhaAgendamentoService.getPilha());
    }
}