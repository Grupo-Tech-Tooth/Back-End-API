package com.example.back.controller;

import com.example.back.dto.Api;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/API")
public class ApiController {
    //cria um cliente http
    static HttpClient client = HttpClient.newHttpClient();

    public static Api[] requisicaoHttp(int quantidade){
        try {
            // cria uma requisição Get
            String url = "https://fakerapi.it/api/v2/persons?_quantity=" + quantidade + "&_locale=pt_BR";
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();

            // envia a requisição e recebe a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //Gson é uma biblioteca que facilita a conversão entre objetos do java para json
            Gson gson = new Gson();

            // acessa o data do json aonde contém os dados do usuario
            String jsonData = gson.fromJson(response.body(), JsonElement.class).getAsJsonObject().get("data").toString();

            // converte o data em uma array de objetos
            Api[] apis = gson.fromJson(jsonData, Api[].class);
            return apis;
        } catch (Exception e) {
            System.out.println("Ocorreu um erro com a Api externa");
            e.printStackTrace();
            return new Api[0];
        }
    }

    @GetMapping("/nao-ordenado/{quantidade}")
    public static ResponseEntity<Api[]> buscarUsuario(
            @PathVariable int quantidade
    ){
        if (quantidade <= 0){
            return ResponseEntity.status(204).build();
        }
        Api[] vetor = requisicaoHttp(quantidade);
        return ResponseEntity.status(200).body(vetor);
    }


    @GetMapping("/ordenado-por-nome/{quantidade}")
    public static ResponseEntity<Api[]> ordenarUsuario(
            @PathVariable int quantidade
    ){
        if (quantidade <= 0){
            return ResponseEntity.status(204).build();
        }
        Api[] vetor = requisicaoHttp(quantidade);
        for (int i = 0; i < vetor.length; i++) {
            int indiceMenor = i;
            for (int j = i + 1; j < vetor.length; j++) {
                if (vetor[j].getFirstname().compareTo(vetor[indiceMenor].getFirstname()) < 0) {
                    indiceMenor = j;
                }
            }
            Api aux = vetor[i];
            vetor[i] = vetor[indiceMenor];
            vetor[indiceMenor] = aux;
        }
        return ResponseEntity.status(200).body(vetor);
    }

    @GetMapping("/ordenado-por-data/{quantidade}")
    public static ResponseEntity<Api[]> ordenarData(
            @PathVariable int quantidade
    ){
        if (quantidade <= 0){
            return ResponseEntity.status(204).build();
        }
        Api[] vetor = requisicaoHttp(quantidade);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < vetor.length; i++) {
            int indiceMenor = i;
            LocalDate dataIndiceMenor = LocalDate.parse(vetor[indiceMenor].getBirthday(), formatter);
            for (int j = i + 1; j < vetor.length; j++) {
                LocalDate dataJ = LocalDate.parse(vetor[j].getBirthday(), formatter);
                if (dataJ.isBefore(dataIndiceMenor)) {
                    indiceMenor = j;
                    dataIndiceMenor = dataJ;
                }
            }

            Api aux = vetor[i];
            vetor[i] = vetor[indiceMenor];
            vetor[indiceMenor] = aux;
        }
        return ResponseEntity.status(200).body(vetor);
    }
}