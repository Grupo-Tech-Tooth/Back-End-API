package com.example.back.controller;

import com.example.back.dto.req.MedicoRequestDto;
import com.example.back.dto.res.MedicoResponseDto;
import com.example.back.entity.LoginInfo;
import com.example.back.entity.Medico;
import com.example.back.repository.MedicoRepository;
import com.example.back.service.MedicoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicoControllerTest {

    @Mock
    private MedicoService medicoService;

    @InjectMocks
    private MedicoController medicoController;

    @Mock
    private MedicoRepository medicoRepository;

    @Test
    @DisplayName("Criar médico deve retornar 201 e o médico criado")
    void criarMedico() {
        MedicoRequestDto request = new MedicoRequestDto();
        request.setNome("Dr. Teste");
        request.setSobrenome("Sobrenome");

        Medico medico = new Medico();
        medico.setNome("Dr. Teste");
        medico.setSobrenome("Sobrenome");

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("teste@gmail.com");
        loginInfo.setSenha("12345678");

        medico.setLoginInfo(loginInfo);

        MedicoResponseDto responseDto = new MedicoResponseDto();
        responseDto.setNome("Dr. Teste");
        responseDto.setEmail("teste@gmail.com");

        when(medicoService.salvarMedico(request)).thenReturn(medico);

        ResponseEntity<MedicoResponseDto> resposta = medicoController.criarMedico(request);

        assertEquals(201, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Dr. Teste", resposta.getBody().getNome());
    }

    @Test
    @DisplayName("Listar médicos deve retornar 204 se a lista estiver vazia")
    void listarMedicosSemResultados() {
        when(medicoService.listarMedicos()).thenReturn(List.of());

        ResponseEntity<List<MedicoResponseDto>> resposta = medicoController.listarMedicos();

        assertEquals(204, resposta.getStatusCodeValue());
        assertNull(resposta.getBody());
    }

    @Test
    @DisplayName("Listar médicos deve retornar 200 com uma lista de médicos")
    void listarMedicosComResultados() {
        Medico medico = new Medico();
        medico.setNome("Dr. Teste");
        medico.setSobrenome("Sobrenome");

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("teste@gmail.com");
        loginInfo.setSenha("12345678");

        medico.setLoginInfo(loginInfo);

        MedicoResponseDto responseDto = new MedicoResponseDto();
        responseDto.setNome("Dr. Teste");

        when(medicoService.listarMedicos()).thenReturn(List.of(medico));

        ResponseEntity<List<MedicoResponseDto>> resposta = medicoController.listarMedicos();

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Dr. Teste", resposta.getBody().get(0).getNome());
    }

    @Test
    @DisplayName("Buscar médico por ID deve retornar 200 se encontrado")
    void buscarMedicoPorIdEncontrado() {
        Long id = 1L;
        Medico medico = new Medico();
        medico.setNome("Dr. Teste");

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("teste@gmail.com");
        loginInfo.setSenha("12345678");

        medico.setLoginInfo(loginInfo);

        MedicoResponseDto responseDto = new MedicoResponseDto();
        responseDto.setNome("Dr. Teste");

        when(medicoService.buscarMedicoPorId(id)).thenReturn(medico);

        ResponseEntity<MedicoResponseDto> resposta = medicoController.buscarMedicoPorId(id);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals("Dr. Teste", resposta.getBody().getNome());
    }

    @Test
    @DisplayName("Deletar médico deve retornar 204 se o médico for deletado")
    void deletarMedico() {
        Long id = 1L;
        Medico medico = new Medico();
        medico.setNome("Dr. Teste");

        doNothing().when(medicoService).deletarMedico(id);

        ResponseEntity<Void> resposta = medicoController.deletarMedico(id);

        assertEquals(204, resposta.getStatusCodeValue());
    }

    @Test
    @DisplayName("Filtrar médicos deve retornar 200 com lista filtrada")
    void filtrarMedicos() {
        String nome = "Dr. Teste";
        MedicoResponseDto responseDto = new MedicoResponseDto();
        responseDto.setNome("Dr. Teste");

        when(medicoService.filtrarMedicos(nome, null, null, null)).thenReturn(List.of(responseDto));

        ResponseEntity<List<MedicoResponseDto>> resposta = medicoController.filtrarMedicos(nome, null, null, null);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().size());
        assertEquals("Dr. Teste", resposta.getBody().get(0).getNome());
    }
}
