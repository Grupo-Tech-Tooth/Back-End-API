package com.example.back.service;

import com.example.back.dto.req.FinanceiroDtoRequest;
import com.example.back.dto.res.FinanceiroResponseDto;
import com.example.back.entity.Financeiro;
import com.example.back.repository.*;
import com.example.back.enums.EspecializacaoOdontologica;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FinanceiroService {

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    public Financeiro criarFinanceiro(FinanceiroDtoRequest financeiroDtoRequest) {
        Double valorCorrigido = 0.0;
        if (financeiroDtoRequest.getTaxas() > 0.0) {
            valorCorrigido = financeiroDtoRequest.getValorBruto() - (financeiroDtoRequest.getValorBruto() * (financeiroDtoRequest.getTaxas() / 100));
        } else {
            valorCorrigido = financeiroDtoRequest.getValorBruto();
        }

        Financeiro financeiro = Financeiro.builder()
                .id(null)
                .dataConsulta(financeiroDtoRequest.getDataConsulta())
                .tratamentoPrincipal((servicoRepository.findById(financeiroDtoRequest.getTratamentoPrincipalId()).orElseThrow()))
                .tratamentoAdicional(servicoRepository.findById(financeiroDtoRequest.getTratamentoAdicionalId()).orElseThrow())
                .cliente(clienteRepository.findById(financeiroDtoRequest.getIdPaciente()).orElseThrow())
                .medico(medicoRepository.findById(financeiroDtoRequest.getIdMedico()).orElseThrow())
                .dataPagamento(financeiroDtoRequest.getDataPagamento())
                .formaPagamento(financeiroDtoRequest.getFormaPagamento())
                .parcelas(financeiroDtoRequest.getParcelas())
                .valorBruto(financeiroDtoRequest.getValorBruto())
                .observacao(financeiroDtoRequest.getObservacao())
                .valorCorrigido(valorCorrigido)
                .taxa(financeiroDtoRequest.getTaxas())
                .deletado(false)
                .build();

        return financeiroRepository.save(financeiro);
    }

    public List<Financeiro> listarFinanceiros() {
        return financeiroRepository.findByAndDeletadoFalse();
    }

    public Financeiro atualizarFinanceiro(Long id, FinanceiroDtoRequest financeiroDtoRequest) {
        Financeiro financeiro = financeiroRepository.findByIdAndDeletadoFalse(id).orElseThrow();

        financeiro.setDataConsulta(financeiroDtoRequest.getDataConsulta());
        financeiro.setCliente(clienteRepository.findById(financeiroDtoRequest.getIdPaciente()).orElseThrow());
        financeiro.setTratamentoPrincipal(servicoRepository.findById(financeiroDtoRequest.getTratamentoPrincipalId()).orElseThrow());
        financeiro.setTratamentoAdicional(servicoRepository.findById(financeiroDtoRequest.getTratamentoAdicionalId()).orElseThrow());
        financeiro.setObservacao(financeiroDtoRequest.getObservacao());
        financeiro.setMedico(medicoRepository.findById(financeiroDtoRequest.getIdMedico()).orElseThrow());
        financeiro.setDataPagamento(financeiroDtoRequest.getDataPagamento());
        financeiro.setFormaPagamento(financeiroDtoRequest.getFormaPagamento());
        financeiro.setParcelas(financeiroDtoRequest.getParcelas());

        Double valorCorrigido = 0.0;
        if (financeiroDtoRequest.getTaxas() > 0.0) {
            valorCorrigido = financeiroDtoRequest.getValorBruto() - (financeiroDtoRequest.getValorBruto() * (financeiroDtoRequest.getTaxas() / 100));
        } else {
            valorCorrigido = financeiroDtoRequest.getValorBruto();
        }

        financeiro.setValorBruto(financeiroDtoRequest.getValorBruto());
        financeiro.setValorCorrigido(valorCorrigido);
        financeiro.setTaxa(financeiroDtoRequest.getTaxas());

        return financeiroRepository.save(financeiro);
    }

    public void deletarFinanceiro(Long id) {
        Financeiro financeiro = financeiroRepository.findByIdAndDeletadoFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Finança não encontrada"));

        financeiro.setDeletado(true);
        financeiro.setDeletadoEm(LocalDateTime.now());

        financeiroRepository.save(financeiro);
    }

    public List<FinanceiroResponseDto> filtrarFinancas(String nomePaciente, LocalDate dataPagamento, String metodoPagamento) {
        return financeiroRepository.findByAndDeletadoFalse().stream()
                .filter(financeiro -> nomePaciente == null || financeiro.getCliente().getNome().toUpperCase().contains(nomePaciente.toUpperCase()))
                .filter(financeiro -> dataPagamento == null || financeiro.getDataPagamento().toLocalDate().equals(dataPagamento))
                .filter(financeiro -> metodoPagamento == null || financeiro.getFormaPagamento().getLabel().toUpperCase().contains(metodoPagamento.toUpperCase()))
                .map(FinanceiroResponseDto::converter)
                .collect(Collectors.toList());
    }

    public List<FinanceiroResponseDto> getFinanceiroData(EspecializacaoOdontologica especializacao) {
        EspecializacaoOdontologica especializacaoEnum = EspecializacaoOdontologica.valueOf(especializacao.name());

        return financeiroRepository.findByMedicoEspecializacao(especializacaoEnum).stream()
                .map(FinanceiroResponseDto::converter)
                .collect(Collectors.toList());
    }

    public List<FinanceiroResponseDto> getFinanceiroTodos() {
        return financeiroRepository.findAll().stream()
                .map(FinanceiroResponseDto::converter)
                .collect(Collectors.toList());
    }

    public Map<DayOfWeek, Double> getSomaTransacoesPorDiaDaSemanaMesAtual() {
        LocalDateTime inicio = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime fim = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(LocalTime.MAX);

        List<Financeiro> transacoes = financeiroRepository.findByDataPagamentoBetween(inicio, fim);
        Map<DayOfWeek, Double> somaPorDiaDaSemana = new EnumMap<>(DayOfWeek.class);

        for (Financeiro transacao : transacoes) {
            DayOfWeek diaDaSemana = transacao.getDataPagamento().getDayOfWeek();
            somaPorDiaDaSemana.put(diaDaSemana, somaPorDiaDaSemana.getOrDefault(diaDaSemana, 0.0) + transacao.getValorBruto());
        }

        return somaPorDiaDaSemana;
    }

    public Map<String, Double> getSomaTransacoesPorSemanaMesAtual() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(startOfMonth.lengthOfMonth());
        Map<String, Double> sumByWeek = new LinkedHashMap<>();

        LocalDate startOfWeek = startOfMonth;
        int week = 1;

        while (startOfWeek.isBefore(endOfMonth) || startOfWeek.isEqual(endOfMonth)) {
            LocalDate endOfWeek = startOfWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            if (endOfWeek.isAfter(endOfMonth)) {
                endOfWeek = endOfMonth;
            }

            LocalDateTime start = startOfWeek.atStartOfDay();
            LocalDateTime end = endOfWeek.atTime(LocalTime.MAX);

            List<Financeiro> transactions = financeiroRepository.findByDataPagamentoBetween(start, end);
            double sumWeek = transactions.stream().mapToDouble(Financeiro::getValorBruto).sum();

            sumByWeek.put("Semana " + week, sumWeek);

            startOfWeek = endOfWeek.plusDays(1);
            week++;
        }

        // Remove a última semana se estiver vazia
        if (sumByWeek.get("Semana " + (week - 1)) == 0.0) {
            sumByWeek.remove("Semana " + (week - 1));
        }

        return sumByWeek;
    }

    public Map<Integer, Double> getSomaTransacoesPorMesAnoAtual() {
        LocalDate inicioAno = LocalDate.now().withDayOfYear(1);
        LocalDate fimAno = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());
        Map<Integer, Double> somaPorMes = new HashMap<>();

        for (int mes = 1; mes <= 12; mes++) {
            LocalDate inicioMes = inicioAno.withMonth(mes).withDayOfMonth(1);
            LocalDate fimMes = inicioAno.withMonth(mes).withDayOfMonth(inicioMes.lengthOfMonth());

            LocalDateTime inicio = inicioMes.atStartOfDay();
            LocalDateTime fim = fimMes.atTime(LocalTime.MAX);

            List<Financeiro> transacoes = financeiroRepository.findByDataPagamentoBetween(inicio, fim);
            double somaMes = transacoes.stream().mapToDouble(Financeiro::getValorBruto).sum();

            somaPorMes.put(mes, somaMes);
        }

        return somaPorMes;
    }
}