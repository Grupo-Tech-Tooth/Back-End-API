package com.example.back.config;

import com.example.back.controller.ClienteController;
import com.example.back.dto.req.*;
import com.example.back.dto.res.ClienteResponseDto;
import com.example.back.entity.*;
import com.example.back.enums.FormaPagamento;
import com.example.back.enums.Hierarquia;
import com.example.back.repository.*;
import com.example.back.service.AgendamentoService;
import com.example.back.service.ClienteService;
import com.example.back.service.FinanceiroService;
import com.example.back.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

// Essa classe ja cria um usuario admin para testes

@Configuration
public class DataLoader implements CommandLineRunner {

    private final LoginInfoRepository loginInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServicoRepository servicoRepository;
    private final FuncionalRepository funcionalRepository;
    private final ClienteRepository clienteRepository;
    private final MedicoRepository medicoRepository;

    @Autowired
    ClienteService clienteService;

    @Autowired
    MedicoService medicoService;

    @Autowired
    AgendamentoService agendamentoService;

    @Autowired
    FinanceiroService financeiroService;

    public DataLoader(LoginInfoRepository loginInfoRepository, PasswordEncoder passwordEncoder, ServicoRepository servicoRepository, FuncionalRepository funcionalRepository, ClienteRepository clienteRepository, MedicoRepository medicoRepository, MedicoRepository medicoRepository1) {
        this.loginInfoRepository = loginInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.servicoRepository = servicoRepository;
        this.funcionalRepository = funcionalRepository;
        this.clienteRepository = clienteRepository;
        this.medicoRepository = medicoRepository1;
    }

    @Override
    public void run(String... args) throws Exception {


        if (!loginInfoRepository.buscarPorEmail("aluno@gmail.com").isPresent()) {
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setEmail("aluno@gmail.com");
            loginInfo.setSenha(passwordEncoder.encode("12345"));
            loginInfoRepository.save(loginInfo);
        }

        if (!loginInfoRepository.buscarPorEmail("yeda@gmail.com").isPresent()) {
            Funcional gerente = new Funcional();
            gerente.setNome("Yeda");
            gerente.setCpf("12345678900");
            gerente.setDepartamento("Gerência");
            gerente.setId(null);

            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setEmail("yeda@gmail.com");
            loginInfo.setSenha(passwordEncoder.encode("123123"));
            loginInfo.setFuncionario(gerente);
            loginInfo.setAtivo(true);

            gerente.setLoginInfo(loginInfo);

            loginInfoRepository.save(loginInfo);
            funcionalRepository.save(gerente);
        }

        if (servicoRepository.count() == 0) {
            Arrays.stream(Servico.Tipo.values())
                    .forEach(tipoCarteira -> servicoRepository.save(tipoCarteira.getServico()));
        }

        if (medicoRepository.count() == 0) {
            List<Medico> medicos = new ArrayList<>();
            List<Cliente> clientes = new ArrayList<>();

            // Criando 10 médicos
            for (int i = 1; i <= 10; i++) {
                MedicoRequestDto medicoRequestDto = new MedicoRequestDto();
                medicoRequestDto.setNome("Medico " + i);
                medicoRequestDto.setSobrenome("Sobrenome " + i);
                medicoRequestDto.setCpf(UUID.randomUUID().toString().substring(0, 10)); // CPF aleatório
                medicoRequestDto.setEmail("medico" + i + "@gmail.com");
                medicoRequestDto.setSenha(passwordEncoder.encode("12345"));

                Medico medicoSalvo = medicoService.salvarMedico(medicoRequestDto);
                medicos.add(medicoSalvo);
            }

            // Criando 10 clientes
            for (int i = 1; i <= 10; i++) {
                SalvarClienteRequestDto clienteRequestDto = new SalvarClienteRequestDto();
                clienteRequestDto.setNome("Cliente " + i);
                clienteRequestDto.setSobrenome("Sobrenome " + i);
                clienteRequestDto.setCpf(UUID.randomUUID().toString().substring(0, 10)); // CPF aleatório
                clienteRequestDto.setEmail("cliente" + i + "@gmail.com");

                // Associando um médico ao cliente
                clienteRequestDto.setMedicoId(medicos.get(i % medicos.size()).getId());

                Cliente clienteSalvo = clienteService.salvarCliente(clienteRequestDto);
                clientes.add(clienteSalvo);
            }

            // Criando 10 agendamentos respeitando horário da clínica e antecipação mínima de 30 minutos
            List<Servico> servicos = servicoRepository.findAll(); // Obtém os serviços disponíveis
            Random random = new Random();

            LocalDateTime dataHora = LocalDateTime.now().plusMinutes(30).withMinute(0).withSecond(0).withNano(0); // Primeira consulta possível

            for (int i = 0; i < 10; i++) {
                // Avança para o próximo dia útil caso seja domingo
                while (dataHora.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    dataHora = dataHora.plusDays(1).withHour(7);
                }

                // Garante que os horários estão entre 07:00 e 19:00
                if (dataHora.getHour() < 7 || dataHora.getHour() >= 19) {
                    dataHora = dataHora.plusDays(1).withHour(7);
                }

                AgendamentoCreateDTO agendamentoDto = new AgendamentoCreateDTO(
                        clientes.get(i).getId(),
                        medicos.get(i % medicos.size()).getId(),
                        servicos.get(random.nextInt(servicos.size())).getId(), // Serviço aleatório
                        "Pendente",
                        dataHora
                );

                AgendamentoDTO agendamentoDTO = agendamentoService.criar(agendamentoDto);

                // Criando o registro financeiro após o agendamento
                FinanceiroDtoRequest financeiroDtoRequest = new FinanceiroDtoRequest();
                financeiroDtoRequest.setIdAgendamento(agendamentoDTO.id());
                financeiroDtoRequest.setIdPaciente(clientes.get(i).getId());
                financeiroDtoRequest.setIdMedico(medicos.get(i % medicos.size()).getId());
                financeiroDtoRequest.setDataPagamento(dataHora.plusDays(1)); // Pagamento no dia seguinte
                financeiroDtoRequest.setFormaPagamento(FormaPagamento.PIX); // Forma de pagamento como cartão
                financeiroDtoRequest.setParcelas(1); // Pagamento à vista
                financeiroDtoRequest.setValorBruto(150.00); // Valor fixo para cada consulta
                financeiroDtoRequest.setTaxas(5.0); // Taxas de 5% para este pagamento
                financeiroDtoRequest.setObservacao("Pagamento referente à consulta");

                // Criando o financeiro
                financeiroService.criarFinanceiro(financeiroDtoRequest);

                // Avança o horário para o próximo agendamento (incrementa de 1 a 3 horas aleatoriamente)
                dataHora = dataHora.plusHours(1 + random.nextInt(3));

                // Garante que os novos agendamentos tenham pelo menos 30 minutos de antecedência
                if (dataHora.isBefore(LocalDateTime.now().plusMinutes(30))) {
                    dataHora = LocalDateTime.now().plusMinutes(30);
                }
            }
        }





    }

}
