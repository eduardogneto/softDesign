package br.com.eduardoneto.aquisicaocredito.exception;

import java.time.LocalDateTime;

public record ErroResponseDTO(
        LocalDateTime dataHora,
        int status,
        String mensagem
) {

    public static ErroResponseDTO de(int status, String mensagem) {
        return new ErroResponseDTO(LocalDateTime.now(), status, mensagem);
    }
}
