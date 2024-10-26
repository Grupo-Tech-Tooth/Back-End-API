package com.example.back.controller;

import com.example.back.controller.dto.AgendamentoCreateDTO;
import com.example.back.controller.dto.AgendamentoDTO;
import com.example.back.entity.Medico;
import com.example.back.entity.Servico;
import com.example.back.infra.execption.BusinessException;
import com.example.back.infra.execption.ResourceNotFoundException;
import com.example.back.observer.LoggerObserver;
import com.example.back.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;
    private LoggerObserver loggerObserver;

    @PostMapping
    public ResponseEntity<AgendamentoDTO> criar(@RequestBody @Valid AgendamentoCreateDTO dto) {
        try {
            AgendamentoDTO agendamento = agendamentoService.criar(dto);
            loggerObserver.onAgendamentoCreated(agendamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(agendamento);
        } catch (BusinessException e) {
            loggerObserver.logBusinessException(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            loggerObserver.logUnexpectedError(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AgendamentoCreateDTO dto) {
        try {
            AgendamentoDTO agendamentoAtualizado = agendamentoService.atualizar(id, dto);
            loggerObserver.onAgendamentoUpdated(agendamentoAtualizado);
            return ResponseEntity.ok(agendamentoAtualizado);
        } catch (ResourceNotFoundException e) {
            loggerObserver.logNotFoundException(id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            loggerObserver.logUnexpectedError(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            agendamentoService.deletar(id);
            loggerObserver.onAgendamentoDeleted(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            loggerObserver.logNotFoundException(id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            loggerObserver.logUnexpectedError(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> buscarPorId(@PathVariable Long id) {
        try {
            AgendamentoDTO agendamentoDTO = agendamentoService.buscarPorId(id);
            loggerObserver.onAgendamentoRetrieved(agendamentoDTO);
            return ResponseEntity.ok(agendamentoDTO);
        } catch (ResourceNotFoundException e) {
            loggerObserver.logNotFoundException(id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            loggerObserver.logUnexpectedError(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<AgendamentoDTO>> buscarPorMedico(@PathVariable Long medicoId) {
        try {
            List<AgendamentoDTO> agendamentos = agendamentoService.buscarPorMedico(medicoId);
            if (agendamentos.isEmpty()) {
                loggerObserver.logNotFoundException(medicoId);  // Log de não encontrado
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Logando cada agendamento recuperado
            agendamentos.forEach(loggerObserver::onAgendamentoRetrieved);

            return ResponseEntity.ok(agendamentos);
        } catch (ResourceNotFoundException e) {
            loggerObserver.logNotFoundException(medicoId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            loggerObserver.logUnexpectedError(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AgendamentoDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        try {
            List<AgendamentoDTO> agendamentos = agendamentoService.buscarPorCliente(clienteId);
            if (agendamentos.isEmpty()) {
                loggerObserver.logNotFoundException(clienteId);  // Log de não encontrado
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Logando cada agendamento recuperado
            agendamentos.forEach(loggerObserver::onAgendamentoRetrieved);

            return ResponseEntity.ok(agendamentos);
        } catch (ResourceNotFoundException e) {
            loggerObserver.logNotFoundException(clienteId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            loggerObserver.logUnexpectedError(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/data")
    public ResponseEntity<List<AgendamentoDTO>> buscarPorData(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            List<AgendamentoDTO> agendamentos = agendamentoService.buscarPorData(data);
            if (agendamentos.isEmpty()) {
                loggerObserver.logBusinessException("Nenhum agendamento encontrado para a data: " + data);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Logando cada agendamento recuperado
            agendamentos.forEach(loggerObserver::onAgendamentoRetrieved);

            return ResponseEntity.ok(agendamentos);
        } catch (Exception e) {
            loggerObserver.logUnexpectedError(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<AgendamentoDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        try {
            List<AgendamentoDTO> agendamentos = agendamentoService.buscarPorPeriodo(inicio, fim);
            if (agendamentos.isEmpty()) {
                loggerObserver.logBusinessException("Nenhum agendamento encontrado para o período: " + inicio + " a " + fim);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Logando cada agendamento recuperado
            agendamentos.forEach(loggerObserver::onAgendamentoRetrieved);

            return ResponseEntity.ok(agendamentos);
        } catch (Exception e) {
            loggerObserver.logUnexpectedError(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/servico")
    public ResponseEntity<List<Servico>> listarServicos() {
        List<Servico> servicos = agendamentoService.listarServicos();
        return servicos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(servicos);
    }
}