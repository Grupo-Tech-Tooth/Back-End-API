package com.example.back.controller;

import com.example.back.dto.req.DadosAutenticacaoReq;
import com.example.back.dto.res.DadosAutenticacaoRes;
import com.example.back.entity.LoginInfo;
import com.example.back.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:3000")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DadosAutenticacaoRes> efetuarLogin(@RequestBody DadosAutenticacaoReq dados){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.gerarToken((LoginInfo) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosAutenticacaoRes(tokenJWT, (LoginInfo) authentication.getPrincipal()));
    }

}
