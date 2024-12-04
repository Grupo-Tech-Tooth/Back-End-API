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

    @Test
    @DisplayName("Buscar cliente por ID deve retornar 400 para ID inválido")
    void buscarClientePorIdIdInvalido() {
        ResponseEntity<ClienteResponseDto> resposta = clienteController.buscarClientePorId(-1L);

        assertEquals(400, resposta.getStatusCodeValue());
        assertNull(resposta.getBody());
    }

    @Test
    @DisplayName("Buscar cliente por ID deve retornar 200 se encontrado")
    void buscarClientePorIdEncontrado() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("teste@gmail.com");
        cliente.setLoginInfo(loginInfo);

        when(clienteService.buscarClientePorId(id)).thenReturn(cliente);

        ResponseEntity<ClienteResponseDto> resposta = clienteController.buscarClientePorId(id);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Cliente Teste", resposta.getBody().getNome());
    }

    @Test
    @DisplayName("Deletar cliente deve retornar 204 se o cliente for deletado")
    void deletarCliente() {
        Long id = 1L;

        doNothing().when(clienteService).deletarClientePorId(id);

        ResponseEntity<Void> resposta = clienteController.deletarClientePorId(id);

        assertEquals(204, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Buscar cliente por nome ou sobrenome deve retornar 200 com resultados")
    void buscarPorNomeOuSobrenome() {
        String nome = "Cliente";
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("teste@gmail.com");
        cliente.setLoginInfo(loginInfo);

        when(clienteService.buscarPorNomeOuSobrenome(nome, null))
                .thenReturn(List.of(new ClienteResponseDto(cliente)));

        ResponseEntity<List<ClienteResponseDto>> resposta = clienteController.buscarPorNomeOuSobrenome(nome, null);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Cliente Teste", resposta.getBody().get(0).getNome());
    }

    @Test
    @DisplayName("Filtrar clientes deve retornar 200 com lista filtrada")
    void filtrarClientes() {
        String nome = "Cliente";
        LocalDate ultimaConsulta = LocalDate.now();
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("teste@gmail.com");
        cliente.setLoginInfo(loginInfo);

        when(clienteService.filtrarClientes(nome, null, null, null))
                .thenReturn(List.of(new ClienteResponseDto(cliente)));

        ResponseEntity<List<ClienteResponseDto>> resposta = clienteController.filtrarClientes(
                nome, null, null, null);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Cliente Teste", resposta.getBody().get(0).getNome());
    }
}
