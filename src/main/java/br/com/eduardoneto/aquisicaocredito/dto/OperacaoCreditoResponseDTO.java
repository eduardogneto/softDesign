package br.com.eduardoneto.aquisicaocredito.dto;

import br.com.eduardoneto.aquisicaocredito.model.Segmento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OperacaoCreditoResponseDTO(
        Long idOperacaoCredito,
        Long idAssociado,
        BigDecimal valorOperacao,
        Segmento segmento,
        String codigoProdutoCredito,
        String codigoConta,
        BigDecimal areaBeneficiadaHa,
        LocalDateTime dataHoraContratacao
) {
}
