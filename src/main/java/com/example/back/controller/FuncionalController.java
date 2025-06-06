package com.example.back.controller;

import com.example.back.dto.req.FuncionalRequestDto;
import com.example.back.dto.res.FuncionalResponseDto;
import com.example.back.entity.Funcional;
import com.example.back.entity.Medico;
import com.example.back.service.FuncionalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.back.enums.Hierarquia.FUNCIONAL;

@RestController
@RequestMapping("/api/v1/funcionais")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "*")
public class FuncionalController {

    @Autowired
    private FuncionalService funcionalService;

    @PostMapping
    public ResponseEntity<FuncionalResponseDto> criarFuncional(@RequestBody @Valid FuncionalRequestDto dto) {

        Funcional novoFuncional = funcionalService.salvarFuncional(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(FuncionalResponseDto.converter(novoFuncional));
    }

    @GetMapping
    public ResponseEntity<List<FuncionalResponseDto>> listarFuncionais() {
        List<Funcional> funcionais = funcionalService.listarFuncionais();

        List<FuncionalResponseDto> funcionaisResponseDto = FuncionalResponseDto.converter(funcionais);

        return funcionais.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(funcionaisResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionalResponseDto> buscarFuncionalPorId(@PathVariable Long id) {
        Funcional funcional = funcionalService.buscarFuncionalPorId(id);

        return ResponseEntity.ok(FuncionalResponseDto.converter(funcional));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionalResponseDto> atualizarFuncional(@PathVariable Long id, @RequestBody FuncionalRequestDto funcionalAtualizado) {

        Funcional funcional = funcionalService.atualizarFuncional(id, funcionalAtualizado);

        FuncionalResponseDto funcionalResponseDto = new FuncionalResponseDto(funcional);

        return ResponseEntity.ok(funcionalResponseDto);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFuncional(@PathVariable Long id) {
        Funcional funcional = funcionalService.buscarFuncionalPorId(id);

            funcionalService.deletarFuncional(id);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/nome")
    public ResponseEntity<List<FuncionalResponseDto>> buscarPorNomeOuSobrenome(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sobrenome
    ) {

        List<Funcional> funcionais = funcionalService.buscarPorNomeOuSobrenome(nome, sobrenome);

        if (funcionais.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(FuncionalResponseDto.converter(funcionais));
        }

    }

    @GetMapping("/email")
    public ResponseEntity<List<FuncionalResponseDto>> buscarPorEmail(
            @RequestParam String email
    ){

        List<Funcional> funcionals = funcionalService.buscarPorEmail(email);

        if (funcionals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok(FuncionalResponseDto.converter(funcionals));
        }
    }

    @GetMapping("/cpf")
    public ResponseEntity<List<FuncionalResponseDto>> buscarPorCpf(
            @RequestParam String cpf
    ){

        List<Funcional> funcionals = funcionalService.buscarPorCpf(cpf);

        if (funcionals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok(FuncionalResponseDto.converter(funcionals));
        }
    }

    @GetMapping("/departamento")
    public ResponseEntity<List<FuncionalResponseDto>> buscarPorDepartamento(
            @RequestParam String departamento
    ){

        List<Funcional> funcionals = funcionalService.buscarPorDepartamento(departamento);

        if (funcionals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok(FuncionalResponseDto.converter(funcionals));
        }
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<FuncionalResponseDto>> filtrarFuncionais(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String departamento) {

        List<FuncionalResponseDto> funcionais = funcionalService.filtrarFuncionais(nome, email, departamento);
        if (funcionais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(funcionais);
    }

}
