package com.example.back.controller;

import com.example.back.dto.req.FuncionalRequestDto;
import com.example.back.dto.res.FuncionalResponseDto;
import com.example.back.entity.Funcional;
import com.example.back.entity.LoginInfo;
import com.example.back.service.FuncionalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionalControllerTest {

    @Mock
    private FuncionalService funcionalService;

    @InjectMocks
    private FuncionalController funcionalController;

    @Test
    @DisplayName("Criar funcional deve retornar 201 e o funcional criado")
    void criarFuncional() {
        FuncionalRequestDto request = new FuncionalRequestDto();
        request.setNome("Teste");
        request.setSobrenome("Sobrenome");
        request.setEmail("teste@gmail.com");
        request.setCpf("12345678900");
        request.setDepartamento("TI");

        Funcional funcional = new Funcional();
        funcional.setNome("Teste");
        funcional.setSobrenome("Sobrenome");

        when(funcionalService.salvarFuncional(request)).thenReturn(funcional);

        ResponseEntity<Funcional> resposta = funcionalController.criarFuncional(request);

        assertEquals(201, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Teste", resposta.getBody().getNome());
    }

    @Test
    @DisplayName("Listar funcionais deve retornar 204 se a lista estiver vazia")
    void listarFuncionaisSemResultados() {
        when(funcionalService.listarFuncionais()).thenReturn(List.of());

        ResponseEntity<List<FuncionalResponseDto>> resposta = funcionalController.listarFuncionais();

        assertEquals(204, resposta.getStatusCodeValue());
        assertNull(resposta.getBody());
    }

    @Test
    @DisplayName("Listar funcionais deve retornar 200 com uma lista de funcionais")
    void listarFuncionaisComResultados() {
        Funcional funcional = new Funcional();
        funcional.setNome("Teste");
        funcional.setSobrenome("Sobrenome");

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("teste@gmail.com");
        loginInfo.setEmail("12345678900");

        funcional.setLoginInfo(loginInfo);

        when(funcionalService.listarFuncionais()).thenReturn(List.of(funcional));

        ResponseEntity<List<FuncionalResponseDto>> resposta = funcionalController.listarFuncionais();

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Teste", resposta.getBody().get(0).getNome());
    }

    @Test
    @DisplayName("Buscar funcional por ID deve retornar 404 se não encontrado")
    void buscarFuncionalPorIdNaoEncontrado() {
        Long id = 1L;
        when(funcionalService.buscarFuncionalPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<Funcional> resposta = funcionalController.buscarFuncionalPorId(id);

        assertEquals(404, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Buscar funcional por ID deve retornar 200 se encontrado")
    void buscarFuncionalPorIdEncontrado() {
        Long id = 1L;
        Funcional funcional = new Funcional();
        funcional.setNome("Teste");
        funcional.setSobrenome("Sobrenome");

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("teste@gmai.com");
        loginInfo.setEmail("12345678900");

        funcional.setLoginInfo(loginInfo);

        when(funcionalService.buscarFuncionalPorId(id)).thenReturn(Optional.of(funcional));

        ResponseEntity<Funcional> resposta = funcionalController.buscarFuncionalPorId(id);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Teste", resposta.getBody().getNome());
    }

    @Test
    @DisplayName("Atualizar funcional deve retornar 200 e o funcional atualizado")
    void atualizarFuncional() {
        Long id = 1L;
        FuncionalRequestDto request = new FuncionalRequestDto();
        request.setNome("Atualizado");
        request.setSobrenome("Sobrenome");
        request.setEmail("teste@gmail.com");
        request.setCpf("12345678900");
        request.setDepartamento("TI");

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("teste@gmail.com");
        loginInfo.setEmail("12345678900");

        Funcional funcionalAtualizado = new Funcional();
        funcionalAtualizado.setNome("Atualizado");
        funcionalAtualizado.setSobrenome("Sobrenome");
        funcionalAtualizado.setLoginInfo(loginInfo);


        when(funcionalService.atualizarFuncional(id, request)).thenReturn(funcionalAtualizado);

        ResponseEntity<FuncionalResponseDto> resposta = funcionalController.atualizarFuncional(id, request);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Atualizado", resposta.getBody().getNome());
    }

    @Test
    @DisplayName("Deletar funcional deve retornar 404 se o funcional não existir")
    void deletarFuncionalNaoEncontrado() {
        Long id = 1L;
        when(funcionalService.buscarFuncionalPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<Void> resposta = funcionalController.deletarFuncional(id);

        assertEquals(404, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Deletar funcional deve retornar 204 se o funcional for deletado")
    void deletarFuncional() {
        Long id = 1L;
        Funcional funcional = new Funcional();
        funcional.setNome("Teste");
        funcional.setSobrenome("Sobrenome");

        when(funcionalService.buscarFuncionalPorId(id)).thenReturn(Optional.of(funcional));
        doNothing().when(funcionalService).deletarFuncional(id);

        ResponseEntity<Void> resposta = funcionalController.deletarFuncional(id);

        assertEquals(204, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Filtrar funcionais deve retornar 200 com lista filtrada")
    void filtrarFuncionais() {
        String nome = "Teste";
        Funcional funcional = new Funcional();
        funcional.setNome("Teste");
        funcional.setSobrenome("Sobrenome");

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("teste@gmail.com");
        loginInfo.setEmail("12345678900");

        funcional.setLoginInfo(loginInfo);

        when(funcionalService.filtrarFuncionais(nome, null, null, null)).thenReturn(List.of(new FuncionalResponseDto(funcional)));

        ResponseEntity<List<FuncionalResponseDto>> resposta = funcionalController.filtrarFuncionais(nome, null, null, null);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Teste", resposta.getBody().get(0).getNome());
    }
}
