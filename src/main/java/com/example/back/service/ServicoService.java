package com.example.back.service;

import com.example.back.dto.req.AgendamentoDTO;
import com.example.back.dto.res.AgendaResponseDto;
import com.example.back.dto.res.ServicoDTO;
import com.example.back.entity.Agendamento;
import com.example.back.entity.Servico;
import com.example.back.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private ServicoRepository servicoRepository;

    public List<ServicoDTO> buscarMaisUsadosMensal() {

        LocalDateTime inicio = LocalDateTime.now().minusMonths(1);
        LocalDateTime fim = LocalDateTime.now();

        List<AgendamentoDTO> consultas = agendamentoService.buscarPorPeriodo(inicio, fim);
        List<ServicoDTO> servicoDTOS = new ArrayList<>();

        for (AgendamentoDTO consulta : consultas) {

            Servico servico = servicoRepository.findById(consulta.servicoId()).orElseThrow();
            ServicoDTO servicoDTO = new ServicoDTO(servico.getNome(), 1);

            if (servicoDTOS.contains(servicoDTO)) {
                servicoDTOS.get(servicoDTOS.indexOf(servicoDTO)).setUsos(servicoDTOS.get(servicoDTOS.indexOf(servicoDTO)).getUsos() + 1);
            } else {
                servicoDTOS.add(servicoDTO);
            }

        }

        return servicoDTOS;

    }

    public List<ServicoDTO> buscarMaisUsadosAnual() {

        LocalDateTime inicio = LocalDateTime.now().minusYears(1);
        LocalDateTime fim = LocalDateTime.now();

        List<AgendamentoDTO> consultas = agendamentoService.buscarPorPeriodo(inicio, fim);
        List<ServicoDTO> servicoDTOS = new ArrayList<>();

        for (AgendamentoDTO consulta : consultas) {

            Servico servico = servicoRepository.findById(consulta.servicoId()).orElseThrow();
            ServicoDTO servicoDTO = new ServicoDTO(servico.getNome(), 1);

            if (servicoDTOS.contains(servicoDTO)) {
                servicoDTOS.get(servicoDTOS.indexOf(servicoDTO)).setUsos(servicoDTOS.get(servicoDTOS.indexOf(servicoDTO)).getUsos() + 1);
            } else {
                servicoDTOS.add(servicoDTO);
            }

        }

        return servicoDTOS;

    }
}
