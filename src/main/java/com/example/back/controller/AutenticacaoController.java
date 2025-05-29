package com.example.back.controller;

import com.example.back.dto.req.DadosAutenticacaoReq;
import com.example.back.dto.res.DadosAutenticacaoRes;
import com.example.back.entity.LoginInfo;
import com.example.back.infra.security.TokenService;
import com.example.back.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login")
@CrossOrigin(origins = "*")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;


    @PostMapping
    public ResponseEntity<DadosAutenticacaoRes> efetuarLogin(@RequestBody DadosAutenticacaoReq dados){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        LoginInfo loginInfo = (LoginInfo) authentication.getPrincipal();
        var tokenJWT = tokenService.gerarToken(loginInfo);

        if (!loginInfo.getAtivo()){
            throw new IllegalArgumentException("Acesso negado");
        }

        emailService.sendEmail(dados.email(), "Login efetuado", "Seu login foi efetuado com sucesso");

        return ResponseEntity.ok(new DadosAutenticacaoRes(tokenJWT, loginInfo));
    }

}
