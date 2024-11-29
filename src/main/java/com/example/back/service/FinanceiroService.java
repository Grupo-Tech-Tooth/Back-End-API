package com.example.back.service;

import com.example.back.dto.req.FinanceiroDtoRequest;
import com.example.back.dto.res.FinanceiroResponseDto;
import com.example.back.entity.Financeiro;
import com.example.back.repository.FinanceiroRepository;
import com.example.back.repository.FuncionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FinanceiroService {

    @Autowired
    private FinanceiroRepository financeiroRepository;

    public Financeiro criarFinanceiro(FinanceiroDtoRequest financeiroDtoRequest) {

        Double valorCorrigido = 0.0;
        if(financeiroDtoRequest.getTaxas()>0.0){
           valorCorrigido =  financeiroDtoRequest.getValorBruto() - (financeiroDtoRequest.getValorBruto() * (financeiroDtoRequest.getTaxas()/100));
        }else {
            valorCorrigido = financeiroDtoRequest.getValorBruto();
        }

        Financeiro financeiro = Financeiro.builder()
                .id(null)
                .dataConsulta(financeiroDtoRequest.getDataConsulta())
                .nomePaciente(financeiroDtoRequest.getNomePaciente())
                .medico(financeiroDtoRequest.getMedico())
                .dataPagamento(financeiroDtoRequest.getDataPagamento())
                .formaPagamento(financeiroDtoRequest.getFormaPagamento())
                .parcelas(financeiroDtoRequest.getParcelas())
                .valorBruto(financeiroDtoRequest.getValorBruto())
                .valorCorrigido(valorCorrigido)
                .cpf(financeiroDtoRequest.getCpf())
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

        Double valorCorrigido = 0.0;
        if(financeiroDtoRequest.getTaxas()>0.0){
            valorCorrigido =  financeiroDtoRequest.getValorBruto() - (financeiroDtoRequest.getValorBruto() * (financeiroDtoRequest.getTaxas()/100));
        }else {
            valorCorrigido = financeiroDtoRequest.getValorBruto();
        }

        financeiro.setDataConsulta(financeiroDtoRequest.getDataConsulta());
        financeiro.setNomePaciente(financeiroDtoRequest.getNomePaciente());
        financeiro.setMedico(financeiroDtoRequest.getMedico());
        financeiro.setDataPagamento(financeiroDtoRequest.getDataPagamento());
        financeiro.setFormaPagamento(financeiroDtoRequest.getFormaPagamento());
        financeiro.setParcelas(financeiroDtoRequest.getParcelas());
        financeiro.setValorBruto(financeiroDtoRequest.getValorBruto());
        financeiro.setValorCorrigido(valorCorrigido);
        financeiro.setTaxa(financeiroDtoRequest.getTaxas());
        financeiro.setCpf(financeiroDtoRequest.getCpf());

        return financeiroRepository.save(financeiro);
    }

    public void deletarFinanceiro(Long id) {
        Financeiro financeiro = financeiroRepository.findByIdAndDeletadoFalse(id)
                .orElseThrow(()-> new IllegalArgumentException("Finança não encontrada"));

        financeiro.setDeletado(true);
        financeiro.setDeletadoEm(LocalDateTime.now());

        financeiroRepository.save(financeiro);
    }

    public List<FinanceiroResponseDto> filtrarFinancas(String nomePaciente, LocalDate dataPagamento, String cpfPaciente, String metodoPagamento){
        return financeiroRepository.findByAndDeletadoFalse().stream()
                .filter(financeiro -> nomePaciente == null || financeiro.getNomePaciente().toUpperCase().contains(nomePaciente.toUpperCase()))
                .filter(financeiro -> dataPagamento == null || financeiro.getDataPagamento().toLocalDate().equals(dataPagamento))
                .filter(financeiro -> cpfPaciente == null || financeiro.getCpf().toUpperCase().contains(cpfPaciente.toUpperCase()))
                .filter(financeiro -> metodoPagamento == null || financeiro.getFormaPagamento().getLabel().toUpperCase().contains(metodoPagamento.toUpperCase()))
                .map(FinanceiroResponseDto::converter)
                .toList();
    }

}
