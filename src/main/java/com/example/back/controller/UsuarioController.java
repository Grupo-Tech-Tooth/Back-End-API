package com.example.back.controller;

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
    public ResponseEntity<Usuario> criarUsuario(@RequestBody @Valid Usuario usuario){

        var novoUsuario = usuarioService.criarUsuario(usuario);

        return ResponseEntity.ok(novoUsuario);

    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obterTodosUsuarios(){

        List<Usuario> usuarios = usuarioService.obterTodosUsuarios();
        return ResponseEntity.ok(usuarios);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obterUsuarioPorId(@PathVariable Long id){

        Optional<Usuario> usuario = usuarioService.obterUsuarioPorId(id);

        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
