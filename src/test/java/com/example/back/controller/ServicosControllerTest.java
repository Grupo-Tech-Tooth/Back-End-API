package com.example.back.controller;

import com.example.back.dto.req.ServicoDtoRequest;
import com.example.back.dto.res.ServicoDTO;
import com.example.back.entity.Servico;
import com.example.back.repository.ServicoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ServicosControllerTest {

    @Mock
    private ServicoRepository servicoRepository;

    @InjectMocks
    private ServicosController servicosController;

    @Test
    @DisplayName("Listar serviços deve retornar 204 se não houver serviços")
    void listarServicosSemResultados() {
        // ARRANGE
        when(servicoRepository.findAll()).thenReturn(List.of());

        // ACT
        ResponseEntity<List<Servico>> resposta = servicosController.listarServicos();

        // ASSERT
        assertEquals(204, resposta.getStatusCodeValue());
        assertTrue(resposta.getBody().isEmpty());
    }

    @Test
    @DisplayName("Listar serviços deve retornar 200 com uma lista de serviços")
    void listarServicosComResultados() {
        // ARRANGE
        Servico servico = new Servico();
        servico.setNome("Teste");
        servico.setDuracaoMinutos(60);

        when(servicoRepository.findAll()).thenReturn(List.of(servico));

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

        when(servicoRepository.save(any(Servico.class))).thenReturn(servico);

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
        Servico servicoAtual = new Servico();
        servicoAtual.setNome("Teste");
        servicoAtual.setDuracaoMinutos(60);
        servicoAtual.setPreco(new BigDecimal("100.0"));
        servicoAtual.setDescricao("Descrição");

        ServicoDtoRequest requestAtualizado = new ServicoDtoRequest("Teste Atualizado", 90, 150.0, "Nova Descrição");

        when(servicoRepository.findById(id)).thenReturn(java.util.Optional.of(servicoAtual));
        when(servicoRepository.save(any(Servico.class))).thenReturn(servicoAtual);

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
        doNothing().when(servicoRepository).deleteById(id);

        // ACT
        ResponseEntity<Void> resposta = servicosController.deletarServico(id);

        // ASSERT
        assertEquals(200, resposta.getStatusCodeValue());
        verify(servicoRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Buscar serviços mais usados deve retornar a lista correta")
    void buscarServicosMaisUsados() {
        // ARRANGE
        ServicoDTO servicoDTO = new ServicoDTO("Teste", 5);
        // ACT
        List<ServicoDTO> resposta = servicosController.buscarServicosMaisUsados("mensal");

        // ASSERT
        assertNotNull(resposta);
        assertEquals(1, resposta.size());
        assertEquals("Teste", resposta.get(0).getNome());
    }

    @Test
    @DisplayName("Filtrar serviços deve retornar 200 com lista filtrada")
    void filtrarServicos() {
        // ARRANGE
        ServicoDtoRequest filtro1 = new ServicoDtoRequest("Teste", 60, 100.0, "Descrição");

        // ACT
        ResponseEntity<List<ServicoDtoRequest>> resposta = servicosController.filtrarServicos(null, null, null, null);

        // ASSERT
        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Teste", resposta.getBody().get(0).nome());
    }
}
