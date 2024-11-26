package com.example.back.controller;

import com.example.back.dto.req.MedicoRequestDto;
import com.example.back.dto.res.MedicoResponseDto;
import com.example.back.entity.LoginInfo;
import com.example.back.entity.Medico;
import com.example.back.service.MedicoService;
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
class MedicoControllerTest {

    @Mock
    private MedicoService medicoService;

    @InjectMocks
    private MedicoController medicoController;

    @Test
    @DisplayName("Criar médico deve retornar 201 e o médico criado")
    void criarMedico() {
        MedicoRequestDto request = new MedicoRequestDto();
        request.setNome("Dr. Teste");
        request.setSobrenome("Sobrenome");
        request.setEmail("dr.teste@gmail.com");
        request.setCpf("12345678900");

        Medico medico = new Medico();
        medico.setNome("Dr. Teste");
        medico.setSobrenome("Sobrenome");

        when(medicoService.salvarMedico(request)).thenReturn(medico);

        ResponseEntity<Medico> resposta = medicoController.criarMedico(request);

        assertEquals(201, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Dr. Teste", resposta.getBody().getNome());
    }

    @Test
    @DisplayName("Listar médicos deve retornar 204 se a lista estiver vazia")
    void listarMedicosSemResultados() {
        when(medicoService.listarMedicos()).thenReturn(List.of());

        ResponseEntity<List<Medico>> resposta = medicoController.listarMedicos();

        assertEquals(204, resposta.getStatusCodeValue());
        assertNull(resposta.getBody());
    }

    @Test
    @DisplayName("Listar médicos deve retornar 200 com uma lista de médicos")
    void listarMedicosComResultados() {
        Medico medico = new Medico();
        medico.setNome("Dr. Teste");
        medico.setSobrenome("Sobrenome");

        when(medicoService.listarMedicos()).thenReturn(List.of(medico));

        ResponseEntity<List<Medico>> resposta = medicoController.listarMedicos();

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Dr. Teste", resposta.getBody().get(0).getNome());
    }

    @Test
    @DisplayName("Buscar médico por ID deve retornar 404 se não encontrado")
    void buscarMedicoPorIdNaoEncontrado() {
        Long id = 1L;
        when(medicoService.buscarMedicoPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<Medico> resposta = medicoController.buscarMedicoPorId(id);

        assertEquals(404, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Buscar médico por ID deve retornar 200 se encontrado")
    void buscarMedicoPorIdEncontrado() {
        Long id = 1L;
        Medico medico = new Medico();
        medico.setNome("Dr. Teste");
        medico.setSobrenome("Sobrenome");

        when(medicoService.buscarMedicoPorId(id)).thenReturn(Optional.of(medico));

        ResponseEntity<Medico> resposta = medicoController.buscarMedicoPorId(id);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Dr. Teste", resposta.getBody().getNome());
    }

    @Test
    @DisplayName("Atualizar médico deve retornar 200 e o médico atualizado")
    void atualizarMedico() {
        Long id = 1L;
        MedicoRequestDto request = new MedicoRequestDto();
        request.setNome("Dr. Atualizado");
        request.setSobrenome("Sobrenome");
        request.setEmail("dr.atualizado@gmail.com");
        request.setCpf("12345678900");

        Medico medicoAtualizado = new Medico();
        medicoAtualizado.setNome("Dr. Atualizado");
        medicoAtualizado.setSobrenome("Sobrenome");

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("teste@gmail.com");
        loginInfo.setSenha("123456");

        medicoAtualizado.setLoginInfo(loginInfo);

        when(medicoService.buscarMedicoPorId(id)).thenReturn(Optional.of(medicoAtualizado));

        when(medicoService.atualizarMedico(id, request)).thenReturn(medicoAtualizado);

        ResponseEntity<Medico> resposta = medicoController.atualizarMedico(id, request);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Dr. Atualizado", resposta.getBody().getNome());
    }

    @Test
    @DisplayName("Deletar médico deve retornar 404 se o médico não existir")
    void deletarMedicoNaoEncontrado() {
        Long id = 1L;
        when(medicoService.buscarMedicoPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<Void> resposta = medicoController.deletarMedico(id);

        assertEquals(404, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Deletar médico deve retornar 204 se o médico for deletado")
    void deletarMedico() {
        Long id = 1L;
        Medico medico = new Medico();
        medico.setNome("Dr. Teste");
        medico.setSobrenome("Sobrenome");

        when(medicoService.buscarMedicoPorId(id)).thenReturn(Optional.of(medico));
        doNothing().when(medicoService).deletarMedico(id);

        ResponseEntity<Void> resposta = medicoController.deletarMedico(id);

        assertEquals(204, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Filtrar médicos deve retornar 200 com lista filtrada")
    void filtrarMedicos() {
        String nome = "Dr. Teste";
        MedicoResponseDto medicoResponseDto = new MedicoResponseDto();
        medicoResponseDto.setNome("Dr. Teste");

        when(medicoService.filtrarMedicos(nome, null, null, null)).thenReturn(List.of(medicoResponseDto));

        ResponseEntity<List<MedicoResponseDto>> resposta = medicoController.filtrarMedicos(nome, null, null, null);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Dr. Teste", resposta.getBody().get(0).getNome());
    }
}
