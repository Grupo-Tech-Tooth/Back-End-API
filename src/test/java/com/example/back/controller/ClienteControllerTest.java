package com.example.back.controller;

import com.example.back.dto.req.AtualizarClienteRequestDto;
import com.example.back.dto.req.SalvarClienteRequestDto;
import com.example.back.dto.res.ClienteResponseDto;
import com.example.back.dto.res.FluxoSemanal;
import com.example.back.entity.Cliente;
import com.example.back.entity.LoginInfo;
import com.example.back.service.ClienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    @DisplayName("Criar cliente deve retornar 201 e o cliente criado")
    void criarCliente() {
        SalvarClienteRequestDto request = new SalvarClienteRequestDto();
        request.setNome("Cliente Teste");
        request.setSobrenome("Sobrenome Teste");

        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");

        when(clienteService.salvarCliente(request)).thenReturn(cliente);

        ResponseEntity<Cliente> resposta = clienteController.criarCliente(request);

        assertEquals(201, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Cliente Teste", resposta.getBody().getNome());
    }

    @Test
    @DisplayName("Listar clientes deve retornar 204 se não houver clientes")
    void listarClientesSemResultados() {
        when(clienteService.listarClientes()).thenReturn(List.of());

        ResponseEntity<List<ClienteResponseDto>> resposta = clienteController.listarClientes();

        assertEquals(204, resposta.getStatusCodeValue());
        assertNull(resposta.getBody());
    }

//    @Test
//    @DisplayName("Buscar cliente por ID deve retornar 400 para ID inválido")
//    void buscarClientePorIdIdInvalido() {
//        ResponseEntity<ClienteResponseDto> resposta = clienteController.buscarClientePorId(-1L);
//
//        assertEquals(400, resposta.getStatusCodeValue());
//        assertNull(resposta.getBody());
//    }

//    @Test
//    @DisplayName("Buscar cliente por ID deve retornar 200 se encontrado")
//    void buscarClientePorIdEncontrado() {
//        Long id = 1L;
//        Cliente cliente = new Cliente();
//        cliente.setNome("Cliente Teste");
//        LoginInfo loginInfo = new LoginInfo();
//        loginInfo.setEmail("teste@gmail.com");
//        cliente.setLoginInfo(loginInfo);
//
//        when(clienteService.buscarClientePorId(id)).thenReturn(cliente);
//
//        ResponseEntity<ClienteResponseDto> resposta = clienteController.buscarClientePorId(id);
//
//        assertEquals(200, resposta.getStatusCodeValue());
//        assertNotNull(resposta.getBody());
//        assertEquals("Cliente Teste", resposta.getBody().getNome());
//    }

    @Test
    @DisplayName("Deletar cliente deve retornar 204 se o cliente for deletado")
    void deletarCliente() {
        Long id = 1L;

        doNothing().when(clienteService).deletarClientePorId(id);

        ResponseEntity<Void> resposta = clienteController.deletarClientePorId(id);

        assertEquals(204, resposta.getStatusCodeValue());
    }

//    @Test
//    @DisplayName("Buscar cliente por nome ou sobrenome deve retornar 200 com resultados")
//    void buscarPorNomeOuSobrenome() {
//        String nome = "Cliente";
//        Cliente cliente = new Cliente();
//        cliente.setNome("Cliente Teste");
//
//        LoginInfo loginInfo = new LoginInfo();
//        loginInfo.setEmail("teste@gmail.com");
//        cliente.setLoginInfo(loginInfo);
//
//        when(clienteService.buscarPorNomeOuSobrenome(nome, null))
//                .thenReturn(List.of(new ClienteResponseDto(cliente)));
//
//        ResponseEntity<List<ClienteResponseDto>> resposta = clienteController.buscarPorNomeOuSobrenome(nome, null);
//
//        assertEquals(200, resposta.getStatusCodeValue());
//        assertNotNull(resposta.getBody());
//        assertEquals(1, resposta.getBody().size());
//        assertEquals("Cliente Teste", resposta.getBody().get(0).getNome());
//    }

//    @Test
//    @DisplayName("Filtrar clientes deve retornar 200 com lista filtrada")
//    void filtrarClientes() {
//        String nome = "Cliente";
//        LocalDate ultimaConsulta = LocalDate.now();
//        Cliente cliente = new Cliente();
//        cliente.setNome("Cliente Teste");
//
//        LoginInfo loginInfo = new LoginInfo();
//        loginInfo.setEmail("teste@gmail.com");
//        cliente.setLoginInfo(loginInfo);
//
//        when(clienteService.filtrarClientes(nome, null, null, null))
//                .thenReturn(List.of(new ClienteResponseDto(cliente)));
//
//        ResponseEntity<List<ClienteResponseDto>> resposta = clienteController.filtrarClientes(
//                nome, null, null, null);
//
//        assertEquals(200, resposta.getStatusCodeValue());
//        assertNotNull(resposta.getBody());
//        assertEquals(1, resposta.getBody().size());
//        assertEquals("Cliente Teste", resposta.getBody().get(0).getNome());
//    }

    @Test
    @DisplayName("Atualizar cliente deve retornar 200 e o cliente atualizado")
    void atualizarClienteComSucesso() {
        Long id = 1L;
        AtualizarClienteRequestDto request = new AtualizarClienteRequestDto();
        request.setNome("Cliente Atualizado");
        request.setSobrenome("Sobrenome Atualizado");

        ClienteResponseDto response = new ClienteResponseDto();
        response.setNome("Cliente Atualizado");
        response.setSobrenome("Sobrenome Atualizado");

        when(clienteService.atualizarCliente(eq(id), any(AtualizarClienteRequestDto.class))).thenReturn(response);

        ResponseEntity<ClienteResponseDto> resposta = clienteController.atualizarCliente(id, request);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Cliente Atualizado", resposta.getBody().getNome());
        assertEquals("Sobrenome Atualizado", resposta.getBody().getSobrenome());
        verify(clienteService, times(1)).atualizarCliente(id, request);
    }

    @Test
    @DisplayName("Atualizar cliente deve lançar exceção para cliente inexistente")
    void atualizarClienteInexistente() {
        Long id = 99L;
        AtualizarClienteRequestDto request = new AtualizarClienteRequestDto();
        request.setNome("Cliente Atualizado");

        when(clienteService.atualizarCliente(eq(id), any(AtualizarClienteRequestDto.class)))
                .thenThrow(new RuntimeException("Cliente não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                clienteController.atualizarCliente(id, request)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clienteService, times(1)).atualizarCliente(id, request);
    }

//    @Test
//    @DisplayName("Buscar clientes com últimos agendamentos deve retornar 200 com resultados")
//    void buscarClientesComUltimosAgendamentosComResultados() {
//        Cliente cliente = new Cliente();
//        cliente.setNome("Cliente Teste");
//
//        LoginInfo loginInfo = new LoginInfo();
//        loginInfo.setEmail("teste@gmail.com");
//        cliente.setLoginInfo(loginInfo);
//
//        ClienteResponseDto response = new ClienteResponseDto(cliente);
//
//        when(clienteService.buscarClientesComUltimosAgendamentos()).thenReturn(List.of(response));
//
//        ResponseEntity<List<ClienteResponseDto>> resposta = clienteController.buscarClientesComUltimosAgendamentos();
//
//        assertEquals(200, resposta.getStatusCodeValue());
//        assertNotNull(resposta.getBody());
//        assertEquals(1, resposta.getBody().size());
//        assertEquals("Cliente Teste", resposta.getBody().get(0).getNome());
//        verify(clienteService, times(1)).buscarClientesComUltimosAgendamentos();
//    }

    @Test
    @DisplayName("Buscar clientes com últimos agendamentos deve retornar 204 sem resultados")
    void buscarClientesComUltimosAgendamentosSemResultados() {
        when(clienteService.buscarClientesComUltimosAgendamentos()).thenReturn(List.of());

        ResponseEntity<List<ClienteResponseDto>> resposta = clienteController.buscarClientesComUltimosAgendamentos();

        assertEquals(204, resposta.getStatusCodeValue());
        assertNull(resposta.getBody());
        verify(clienteService, times(1)).buscarClientesComUltimosAgendamentos();
    }

    @Test
    @DisplayName("Buscar fluxo mensal deve retornar 204 quando não houver dados")
    void buscarFluxoMensalSemDados() {
        when(clienteService.buscarFluxoMensal()).thenReturn(null);

        ResponseEntity<FluxoSemanal> resposta = clienteController.buscarFluxoMensal();

        assertEquals(204, resposta.getStatusCodeValue());
        assertNull(resposta.getBody());
        verify(clienteService, times(1)).buscarFluxoMensal();
    }

}
