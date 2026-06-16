package br.com.eduardoneto.aquisicaocredito.dto;

import br.com.eduardoneto.aquisicaocredito.model.Segmento;

import java.math.BigDecimal;

public record OperacaoCreditoRequestDTO(
        Long idAssociado,
        BigDecimal valorOperacao,
        Segmento segmento,
        String codigoProdutoCredito,
        String codigoConta,
        BigDecimal areaBeneficiadaHa
) {
}
