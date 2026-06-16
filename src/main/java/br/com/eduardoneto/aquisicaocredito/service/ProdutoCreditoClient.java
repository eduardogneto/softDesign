package br.com.eduardoneto.aquisicaocredito.service;

import br.com.eduardoneto.aquisicaocredito.exception.ServicoProdutoCreditoIndisponivelException;
import br.com.eduardoneto.aquisicaocredito.model.Segmento;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

@Component
public class ProdutoCreditoClient implements ProdutoCreditoGateway {

    private static final int MAX_TENTATIVAS = 3;
    private static final long INTERVALO_BASE_MS = 300L;

    private final RestClient restClient;

    public ProdutoCreditoClient(RestClient.Builder restClientBuilder,
                                 @Value("${produtocredito.api.base-url}") String baseUrl) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    public boolean permiteContratacao(String codigoProdutoCredito, Segmento segmento, BigDecimal valorOperacao) {
        RestClientException ultimaFalha = null;

        for (int tentativa = 1; tentativa <= MAX_TENTATIVAS; tentativa++) {
            try {
                PermissaoContratacaoResponse resposta = restClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/produtos-credito/{codigoProdutoCredito}/permite-contratacao")
                                .queryParam("segmento", segmento)
                                .queryParam("valorFinanciado", valorOperacao.toPlainString())
                                .build(codigoProdutoCredito))
                        .retrieve()
                        .body(PermissaoContratacaoResponse.class);

                return resposta != null && resposta.permiteContratar();
            } catch (RestClientException ex) {
                ultimaFalha = ex;
                aguardarAntesDeNovaTentativa(tentativa);
            }
        }

        throw new ServicoProdutoCreditoIndisponivelException(
                "Serviço de produtos de crédito indisponível após " + MAX_TENTATIVAS + " tentativas",
                ultimaFalha);
    }

    private void aguardarAntesDeNovaTentativa(int tentativa) {
        if (tentativa == MAX_TENTATIVAS) {
            return;
        }
        try {
            Thread.sleep(INTERVALO_BASE_MS * tentativa);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private record PermissaoContratacaoResponse(boolean permiteContratar) {
    }
}
