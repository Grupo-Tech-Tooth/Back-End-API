package com.example.back.service;

import com.example.back.controller.dto.CriarUsuarioDto;
import com.example.back.entity.Usuario;
import com.example.back.exception.UsuarioExistenteException;
import com.example.back.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;


    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario(CriarUsuarioDto dto){

        var usuarioDb = usuarioRepository.findByCpf(dto.cpf());

        if (usuarioDb.isPresent()){
            throw new UsuarioExistenteException("Usuario já existe com esse CPF");
        }

        Usuario usuario = dto.toUsuario();

        return usuarioRepository.save(usuario);
    }

}
