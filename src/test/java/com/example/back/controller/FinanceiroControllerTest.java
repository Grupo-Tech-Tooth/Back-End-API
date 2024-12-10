package com.example.back.controller;

import com.example.back.dto.req.FinanceiroDtoRequest;
import com.example.back.dto.res.FinanceiroResponseDto;
import com.example.back.entity.Financeiro;
import com.example.back.service.FinanceiroService;
import com.example.back.enums.FormaPagamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class FinanceiroControllerTest {

    @Mock
    private FinanceiroService financeiroService;

    @InjectMocks
    private FinanceiroController financeiroController;

//    @Test
//    @DisplayName("Listar financeiro deve retornar 204 se não houver registros")
//    void listarFinanceiroSemResultados() {
//        // ARRANGE
//        when(financeiroService.listarFinanceiros()).thenReturn(List.of());
//
//        // ACT
//        ResponseEntity<List<FinanceiroResponseDto>> resposta = financeiroController.listarFinanceiros();
//
//        // ASSERT
//        assertEquals(204, resposta.getStatusCodeValue());
//        assertNull(resposta.getBody());
//    }

//    @Test
//    @DisplayName("Listar financeiro deve retornar 200 com uma lista de registros")
//    void listarFinanceiroComResultados() {
//        // ARRANGE
//        Financeiro financeiro = new Financeiro();
//        financeiro.setId(1L);
//        financeiro.setValorBruto(100.0);
//
//        // ACT
//        ResponseEntity<List<FinanceiroResponseDto>> resposta = financeiroController.listarFinanceiros();
//
//        // ASSERT
//        assertEquals(200, resposta.getStatusCodeValue());
//        assertNotNull(resposta.getBody());
//        assertEquals(1, resposta.getBody().size());
//        assertEquals(1L, resposta.getBody().get(0).getId());
//    }

    @Test
    @DisplayName("Cadastrar financeiro deve retornar 200 e salvar o registro")
    void cadastrarFinanceiroComSucesso() {
        // ARRANGE
        FinanceiroDtoRequest request = new FinanceiroDtoRequest();
        request.setIdPaciente(1L);
        request.setIdMedico(1L);
        request.setDataPagamento(LocalDateTime.now());
        request.setFormaPagamento(FormaPagamento.CARTAO_CREDITO);
        request.setParcelas(3);
        request.setValorBruto(100.0);

        Financeiro financeiro = new Financeiro();
        financeiro.setId(1L);
        financeiro.setValorBruto(100.0);

        when(financeiroService.criarFinanceiro(request)).thenReturn(financeiro);

        // ACT
        ResponseEntity<Financeiro> resposta = financeiroController.criarFinanceiro(request);

        // ASSERT
        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1L, resposta.getBody().getId());
    }

    @Test
    @DisplayName("Atualizar financeiro deve retornar 200 e atualizar o registro")
    void atualizarFinanceiroComSucesso() {
        // ARRANGE
        Long id = 1L;
        FinanceiroDtoRequest requestAtualizado = new FinanceiroDtoRequest();
        requestAtualizado.setIdPaciente(1L);
        requestAtualizado.setIdMedico(1L);
        requestAtualizado.setDataPagamento(LocalDateTime.now());
        requestAtualizado.setFormaPagamento(FormaPagamento.CARTAO_DEBITO);
        requestAtualizado.setParcelas(3);
        requestAtualizado.setValorBruto(150.0);

        Financeiro financeiroAtualizado = new Financeiro();
        financeiroAtualizado.setId(id);
        financeiroAtualizado.setValorBruto(150.0);

        when(financeiroService.atualizarFinanceiro(id, requestAtualizado)).thenReturn(financeiroAtualizado);

        // ACT
        ResponseEntity<Financeiro> resposta = financeiroController.atualizarFinanceiro(id, requestAtualizado);

        // ASSERT
        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(150.0, resposta.getBody().getValorBruto());
    }

    @Test
    @DisplayName("Deletar financeiro com sucesso")
    void deletarFinanceiroComSucesso() {
        // ARRANGE
        Long id = 1L;
        doNothing().when(financeiroService).deletarFinanceiro(id);

        // ACT
        ResponseEntity<FinanceiroResponseDto> resposta = financeiroController.deletarFinanceiro(id);

        // ASSERT
        assertEquals(204, resposta.getStatusCodeValue());
        verify(financeiroService, times(1)).deletarFinanceiro(id);
    }

    @Test
    @DisplayName("Filtrar financeiro deve retornar 200 com lista filtrada")
    void filtrarFinanceiro() {
        // ARRANGE
        FinanceiroResponseDto filtro1 = new FinanceiroResponseDto();
        filtro1.setValorBruto(100.0);

        when(financeiroService.filtrarFinancas(null, null, null)).thenReturn(List.of(filtro1));

        // ACT
        ResponseEntity<List<FinanceiroResponseDto>> resposta = financeiroController.filtrarFinanceiro(null, null, null);

        // ASSERT
        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
    }
}
