package br.com.eduardoneto.aquisicaocredito.service;

import br.com.eduardoneto.aquisicaocredito.exception.ServicoProdutoCreditoIndisponivelException;
import br.com.eduardoneto.aquisicaocredito.model.Segmento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ProdutoCreditoClientTest {

    private static final String BASE_URL = "http://localhost";
    private static final String CAMINHO_ESPERADO = BASE_URL + "/produtos-credito/909I/permite-contratacao";

    private MockRestServiceServer mockServer;
    private ProdutoCreditoClient produtoCreditoClient;

    @BeforeEach
    void configurar() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        produtoCreditoClient = new ProdutoCreditoClient(restClientBuilder, BASE_URL);
    }

    @Test
    void permiteContratacao_deveRetornarTrue_quandoApiPermiteContratacao() {
        mockServer.expect(requestTo(startsWith(CAMINHO_ESPERADO)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"permiteContratar\": true}", MediaType.APPLICATION_JSON));

        boolean resultado = produtoCreditoClient.permiteContratacao("909I", Segmento.AGRO, new BigDecimal("3000"));

        assertThat(resultado).isTrue();
        mockServer.verify();
    }

    @Test
    void permiteContratacao_deveRetornarFalse_quandoApiNaoPermiteContratacao() {
        mockServer.expect(requestTo(startsWith(CAMINHO_ESPERADO)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"permiteContratar\": false}", MediaType.APPLICATION_JSON));

        boolean resultado = produtoCreditoClient.permiteContratacao("909I", Segmento.AGRO, new BigDecimal("3000"));

        assertThat(resultado).isFalse();
        mockServer.verify();
    }

    @Test
    void permiteContratacao_deveLancarExcecao_quandoApiFalhaEmTodasAsTentativas() {
        mockServer.expect(requestTo(startsWith(CAMINHO_ESPERADO)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());
        mockServer.expect(requestTo(startsWith(CAMINHO_ESPERADO)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());
        mockServer.expect(requestTo(startsWith(CAMINHO_ESPERADO)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        assertThatThrownBy(() -> produtoCreditoClient.permiteContratacao("909I", Segmento.AGRO, new BigDecimal("3000")))
                .isInstanceOf(ServicoProdutoCreditoIndisponivelException.class);

        mockServer.verify();
    }
}
