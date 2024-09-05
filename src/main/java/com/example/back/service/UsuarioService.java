package com.example.back.service;

import com.example.back.entity.Usuario;
import com.example.back.exception.UsuarioExistenteException;
import com.example.back.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;


    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario(Usuario usuarioCadastrar){

        var usuarioDb = usuarioRepository.findByCpf(usuarioCadastrar.getCpf());

        if (usuarioDb.isPresent()){
            throw new UsuarioExistenteException("Usuario já existe com esse CPF");
        }

        return usuarioRepository.save(usuarioCadastrar);
    }

    public List<Usuario> obterTodosUsuarios(){

        return usuarioRepository.findAll();

    }

    public Optional<Usuario> obterUsuarioPorId(Long id){

        return usuarioRepository.findById(id);

    }

}
