package com.example.back.controller;

import com.example.back.service.AgendamentoDeAvisos;
import com.example.back.service.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emails")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private AgendamentoDeAvisos enviarEmail;

    @GetMapping
    public ResponseEntity<String> enviarEmail(){

        enviarEmail.enviarAvisosUmDiaAntes();
        enviarEmail.enviarAvisosNoDia();

        return ResponseEntity.ok("Foi");

    }
}
