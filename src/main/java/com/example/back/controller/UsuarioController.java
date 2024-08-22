package com.example.back.controller;

import com.example.back.controller.dto.CriarUsuarioDto;
import com.example.back.entity.Usuario;
import com.example.back.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody @Valid CriarUsuarioDto dto){

        var usuario = usuarioService.criarUsuario(dto);

        return ResponseEntity.ok(usuario);

    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obterTodosUsuarios(){

        List<Usuario> usuarios = usuarioService.obterTodosUSuarios();
        return ResponseEntity.ok(usuarios);

    }


}
