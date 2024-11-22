package com.example.back.controller;

import com.example.back.dto.req.ServicoDtoRequest;
import com.example.back.dto.res.ServicoDTO;
import com.example.back.entity.Servico;
import com.example.back.service.ServicoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ServicosControllerTest {

    @Mock
    private ServicoService servicoService;

    @InjectMocks
    private ServicosController servicosController;

    @Test
    @DisplayName("Listar serviços deve retornar 204 se não houver serviços")
    void listarServicosSemResultados() {
        // ARRANGE
        when(servicoService.listarServicos()).thenReturn(List.of());

        // ACT
        ResponseEntity<List<Servico>> resposta = servicosController.listarServicos();

        // ASSERT
        assertEquals(204, resposta.getStatusCodeValue());
        assertNull(resposta.getBody());
    }

    @Test
    @DisplayName("Listar serviços deve retornar 200 com uma lista de serviços")
    void listarServicosComResultados() {
        // ARRANGE
        Servico servico = new Servico();
        servico.setNome("Teste");
        servico.setDuracaoMinutos(60);

        when(servicoService.listarServicos()).thenReturn(List.of(servico));

        // ACT
        ResponseEntity<List<Servico>> resposta = servicosController.listarServicos();

        // ASSERT
        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Teste", resposta.getBody().get(0).getNome());
    }

    @Test
    @DisplayName("Cadastrar serviço deve retornar 200 e salvar o serviço")
    void cadastrarServicoComSucesso() {
        // ARRANGE
        ServicoDtoRequest request = new ServicoDtoRequest("Teste", 60, 100.0, "Descrição");
        Servico servico = new Servico();
        servico.setNome("Teste");
        servico.setDuracaoMinutos(60);
        servico.setPreco(new BigDecimal("100.0"));

        when(servicoService.cadastrarServico(request)).thenReturn(servico);

        // ACT
        ResponseEntity<Servico> resposta = servicosController.cadastrarServico(request);

        // ASSERT
        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Teste", resposta.getBody().getNome());
    }

    @Test
    @DisplayName("Atualizar serviço deve retornar 200 e atualizar o serviço")
    void atualizarServicoComSucesso() {
        // ARRANGE
        Long id = 1L;
        ServicoDtoRequest requestAtualizado = new ServicoDtoRequest("Teste Atualizado", 90, 150.0, "Nova Descrição");
        Servico servicoAtualizado = new Servico();
        servicoAtualizado.setNome("Teste Atualizado");
        servicoAtualizado.setDuracaoMinutos(90);

        when(servicoService.atualizarServico(id, requestAtualizado)).thenReturn(servicoAtualizado);

        // ACT
        ResponseEntity<Servico> resposta = servicosController.atualizarServico(id, requestAtualizado);

        // ASSERT
        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Teste Atualizado", resposta.getBody().getNome());
    }

    @Test
    @DisplayName("Deletar serviço com sucesso")
    void deletarServicoComSucesso() {
        // ARRANGE
        Long id = 1L;
        doNothing().when(servicoService).deletarServico(id);

        // ACT
        ResponseEntity<Void> resposta = servicosController.deletarServico(id);

        // ASSERT
        assertEquals(204, resposta.getStatusCodeValue());
        verify(servicoService, times(1)).deletarServico(id);
    }

    @Test
    @DisplayName("Buscar serviços mais usados deve retornar a lista correta")
    void buscarServicosMaisUsados() {
        // ARRANGE
        ServicoDTO servicoDTO = new ServicoDTO("Teste", 5);
        when(servicoService.buscarMaisUsadosMensal()).thenReturn(List.of(servicoDTO));

        // ACT
        ResponseEntity<List<ServicoDTO>> resposta = servicosController.buscarServicosMaisUsados("mensal");

        // ASSERT
        assertNotNull(resposta);
        assertEquals(1, resposta.getBody().size());
        assertEquals("Teste", resposta.getBody().get(0).getNome());
    }

    @Test
    @DisplayName("Filtrar serviços deve retornar 200 com lista filtrada")
    void filtrarServicos() {
        // ARRANGE
        ServicoDtoRequest filtro1 = new ServicoDtoRequest("Teste", 60, 100.0, "Descrição");
        when(servicoService.filtrarServicos(null, null, null, null)).thenReturn(List.of(filtro1));

        // ACT
        ResponseEntity<List<ServicoDtoRequest>> resposta = servicosController.filtrarServicos(null, null, null, null);

        // ASSERT
        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Teste", resposta.getBody().get(0).nome());
    }
}
