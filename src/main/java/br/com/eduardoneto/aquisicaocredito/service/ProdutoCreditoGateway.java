package br.com.eduardoneto.aquisicaocredito.service;

import br.com.eduardoneto.aquisicaocredito.model.Segmento;

import java.math.BigDecimal;

public interface ProdutoCreditoGateway {

    boolean permiteContratacao(String codigoProdutoCredito, Segmento segmento, BigDecimal valorOperacao);
}
