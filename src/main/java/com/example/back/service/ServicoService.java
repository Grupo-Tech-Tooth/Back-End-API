package com.example.back.service;

import com.example.back.dto.req.AgendamentoDTO;
import com.example.back.dto.req.ServicoDtoRequest;
import com.example.back.dto.res.AgendaResponseDto;
import com.example.back.dto.res.ServicoDTO;
import com.example.back.entity.Agendamento;
import com.example.back.entity.Servico;
import com.example.back.enums.Categoria;
import com.example.back.infra.execption.ResourceNotFoundException;
import com.example.back.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServicoService {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private ServicoRepository servicoRepository;

    public List<ServicoDTO> buscarMaisUsadosMensal() {

        //Inicio do mes atual e fim do mes atual

        LocalDateTime inicio = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fim = LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);


        System.out.println("Inicio: " + inicio);
        System.out.println("Fim: " + fim);

        List<AgendamentoDTO> consultas = agendamentoService.buscarPorPeriodo(inicio, fim);


        Map<String, ServicoDTO> servicoMap = new HashMap<>();

        for (AgendamentoDTO consulta : consultas) {
            Servico servico = servicoRepository.findById(consulta.servico().getId()).orElseThrow();
            servicoMap.compute(servico.getNome(), (nome, dto) -> {
                if (dto == null) {
                    return new ServicoDTO(nome, 1);
                } else {
                    dto.setUsos(dto.getUsos() + 1);
                    return dto;
                }
            });
        }

        return new ArrayList<>(servicoMap.values());
    }


    public List<ServicoDTO> buscarMaisUsadosAnual() {

        LocalDateTime inicio = LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fim = LocalDateTime.now().withDayOfYear(LocalDateTime.now().toLocalDate().lengthOfYear()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<AgendamentoDTO> consultas = agendamentoService.buscarPorPeriodo(inicio, fim);
        List<ServicoDTO> servicoDTOS = new ArrayList<>();

        for (AgendamentoDTO consulta : consultas) {

            Servico servico = servicoRepository.findById(consulta.servico().getId()).orElseThrow();
            ServicoDTO servicoDTO = new ServicoDTO(servico.getNome(), 1);

            if (servicoDTOS.contains(servicoDTO)) {
                servicoDTOS.get(servicoDTOS.indexOf(servicoDTO)).setUsos(servicoDTOS.get(servicoDTOS.indexOf(servicoDTO)).getUsos() + 1);
            } else {
                servicoDTOS.add(servicoDTO);
            }

        }

        return servicoDTOS;

    }


    public List<ServicoDtoRequest> filtrarServicos(String nome, Integer duracao, BigDecimal preco, Categoria categoria) {
        List<Servico> servicos = servicoRepository.filtrarServicos(nome, duracao, preco, categoria);

        return servicos.stream()
                .map(servico -> new ServicoDtoRequest(
                        servico.getNome(),
                        servico.getDuracaoMinutos(),
                        servico.getPreco().doubleValue(),
                        servico.getDescricao(),
                        servico.getCategoria()))
                .toList();
    }


    public Servico cadastrarServico(ServicoDtoRequest servicoDtoRequest) {
        Servico servico = Servico.builder()
                .nome(servicoDtoRequest.nome())
                .duracaoMinutos(servicoDtoRequest.duracaoMinutos())
                .preco(BigDecimal.valueOf(servicoDtoRequest.preco()))
                .descricao(servicoDtoRequest.descricao())
                .deletado(false)
                .categoria(servicoDtoRequest.categoria())
                .build();

        return servicoRepository.save(servico);
    }

    public Servico atualizarServico(Long id, ServicoDtoRequest servicoDtoRequest) {
        Servico servico = servicoRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Serviço não encontrado"));

        servico.setNome(servicoDtoRequest.nome());
        servico.setDuracaoMinutos(servicoDtoRequest.duracaoMinutos());
        servico.setPreco(BigDecimal.valueOf(servicoDtoRequest.preco()));
        servico.setDescricao(servicoDtoRequest.descricao());
        servico.setCategoria(servicoDtoRequest.categoria());

        return servicoRepository.save(servico);
    }

    public void deletarServico(Long id) {

        if (servicoRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        Servico servico = servicoRepository.findById(id).orElseThrow();
        servico.setDeletado(true);
        servico.setDeletadoEm(LocalDateTime.now());

        servicoRepository.save(servico);
    }

    public List<Servico> listarServicos() {
        List<Servico> servicos = servicoRepository.findByDeletadoFalse();

        if (servicos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum serviço encontrado");
        }

        return servicos;
    }
}
