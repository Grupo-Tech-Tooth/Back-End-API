package com.example.back.controller;

import com.example.back.dto.req.AgendamentoCreateDTO;
import com.example.back.dto.req.AgendamentoDTO;
import com.example.back.dto.res.ClienteResponseDto;
import com.example.back.entity.Agendamento;
import com.example.back.entity.Servico;
import com.example.back.service.AgendamentoService;
import com.example.back.observer.LoggerObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgendamentoControllerTest {

    @InjectMocks
    private AgendamentoController agendamentoController;

    @Mock
    private AgendamentoService agendamentoService;

    @Mock
    private LoggerObserver loggerObserver;

    public AgendamentoControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    // Métodos utilitários para criar DTOs
    private AgendamentoCreateDTO createAgendamentoCreateDTO() {
        return new AgendamentoCreateDTO(
                1L, // clienteId
                2L, // medicoId
                3L, // servicoId
                "PENDENTE", // status
                LocalDateTime.now() // dataHora
        );
    }

    private AgendamentoDTO createAgendamentoDTO() {
        return new AgendamentoDTO(
                1L, // id
                (ClienteResponseDto) null, // cliente
                null, // medico
                null, // servico
                "PENDENTE", // status
                LocalDateTime.now() // dataHora
        );
    }

    @Test
    @DisplayName("Criar agendamento deve retornar status 201")
    void criarAgendamentoDeveRetornar201() {
        // ARRANGE
        AgendamentoCreateDTO createDTO = createAgendamentoCreateDTO();
        AgendamentoDTO agendamentoDTO = createAgendamentoDTO();
        when(agendamentoService.criar(createDTO)).thenReturn(agendamentoDTO);

        // ACT
        ResponseEntity<AgendamentoDTO> response = agendamentoController.criar(createDTO);

        // ASSERT
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(agendamentoDTO, response.getBody());
    }

    @Test
    @DisplayName("Buscar todos deve retornar lista vazia com status 204")
    void buscarTodosDeveRetornar204SeListaVazia() {
        // ARRANGE
        when(agendamentoService.buscarTodosAgendamentos()).thenReturn(Collections.emptyList());

        // ACT
        ResponseEntity<List<AgendamentoDTO>> response = agendamentoController.buscarTodos();

        // ASSERT
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(loggerObserver, times(1)).logBusinessException("Nenhum agendamento encontrado");
    }


    @Test
    @DisplayName("Atualizar agendamento deve retornar status 200")
    void atualizarAgendamentoDeveRetornar200() {
        // ARRANGE
        Long id = 1L;
        AgendamentoCreateDTO createDTO = createAgendamentoCreateDTO();
        AgendamentoDTO agendamentoDTO = createAgendamentoDTO();
        when(agendamentoService.atualizar(id, createDTO)).thenReturn(agendamentoDTO);

        // ACT
        ResponseEntity<AgendamentoDTO> response = agendamentoController.atualizar(id, createDTO);

        // ASSERT
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(agendamentoDTO, response.getBody());
    }

    @Test
    @DisplayName("Deletar agendamento deve retornar status 200")
    void deletarAgendamentoDeveRetornar200() {
        // ARRANGE
        Long id = 1L;
        AgendamentoDTO agendamentoDTO = createAgendamentoDTO();
        when(agendamentoService.deletar(id)).thenReturn(agendamentoDTO);

        // ACT
        ResponseEntity<AgendamentoDTO> response = agendamentoController.deletar(id);

        // ASSERT
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(agendamentoDTO, response.getBody());
    }

    @Test
    @DisplayName("Cancelar consulta deve retornar status 200")
    void cancelarConsultaDeveRetornar200() {
        // ARRANGE
        Long id = 1L;
        AgendamentoDTO agendamentoDTO = createAgendamentoDTO();
        when(agendamentoService.cancelarConsulta(id)).thenReturn(agendamentoDTO);

        // ACT
        ResponseEntity<AgendamentoDTO> response = agendamentoController.cancelarConsulta(id);

        // ASSERT
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(agendamentoDTO, response.getBody());
    }

    @Test
    @DisplayName("Listar serviços deve retornar lista de serviços com status 200")
    void listarServicosDeveRetornar200() {
        // ARRANGE
        List<Servico> servicos = Collections.singletonList(new Servico());
        when(agendamentoService.listarServicos()).thenReturn(servicos);

        // ACT
        ResponseEntity<List<Servico>> response = agendamentoController.listarServicos();

        // ASSERT
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(servicos, response.getBody());
    }

    @Test
    @DisplayName("Buscar último agendamento de cliente deve retornar o agendamento")
    void buscarUltimoAgendamentoDeClienteDeveRetornarAgendamento() {
        // ARRANGE
        Long clienteId = 1L;
        AgendamentoDTO agendamentoDTO = createAgendamentoDTO();
        when(agendamentoService.buscarUltimoAgendamentoDeCliente(clienteId)).thenReturn(Optional.of(agendamentoDTO));

        // ACT
        ResponseEntity<Optional<AgendamentoDTO>> response = agendamentoController.buscarUltimoAgendamentoDeCliente(clienteId);

        // ASSERT
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Aqui você compara o conteúdo do Optional
        assertEquals(agendamentoDTO, response.getBody().orElseThrow());
    }

}
