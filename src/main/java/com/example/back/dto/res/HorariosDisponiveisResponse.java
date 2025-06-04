package com.example.back.dto.res;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class HorariosDisponiveisResponse {
    private List<String> horariosDisponiveis;

    // Construtor personalizado para converter List<LocalTime> em List<String>
    public HorariosDisponiveisResponse(List<LocalTime> horariosDisponiveis) {
        this.horariosDisponiveis = horariosDisponiveis.stream()
                .map(LocalTime::toString) // Converte LocalTime para String (HH:mm:ss)
                .map(horario -> horario.substring(0, 5)) // Remove os segundos (formato HH:mm)
                .collect(Collectors.toList());
    }

    // Construtor padrão (necessário para serialização/desserialização)
    public HorariosDisponiveisResponse() {
    }
}