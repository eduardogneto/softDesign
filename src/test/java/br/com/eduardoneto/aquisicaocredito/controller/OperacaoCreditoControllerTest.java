package br.com.eduardoneto.aquisicaocredito.controller;

import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoRequestDTO;
import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoResponseDTO;
import br.com.eduardoneto.aquisicaocredito.exception.ContratoRecusadoException;
import br.com.eduardoneto.aquisicaocredito.exception.OperacaoCreditoNaoEncontradaException;
import br.com.eduardoneto.aquisicaocredito.mapper.OperacaoCreditoMapper;
import br.com.eduardoneto.aquisicaocredito.model.OperacaoCredito;
import br.com.eduardoneto.aquisicaocredito.model.Segmento;
import br.com.eduardoneto.aquisicaocredito.service.OperacaoCreditoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OperacaoCreditoController.class)
class OperacaoCreditoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OperacaoCreditoService operacaoCreditoService;

    @MockitoBean
    private OperacaoCreditoMapper mapper;

    @Test
    void contratar_deveRetornarCreated_quandoOperacaoForAceita() throws Exception {
        OperacaoCreditoRequestDTO requestDTO = new OperacaoCreditoRequestDTO(
                1L, new BigDecimal("1000"), Segmento.PF, "101A", "1234567890", null);

        OperacaoCredito operacaoCredito = new OperacaoCredito();
        operacaoCredito.setIdOperacaoCredito(1L);

        when(operacaoCreditoService.contratar(any(OperacaoCreditoRequestDTO.class))).thenReturn(operacaoCredito);

        mockMvc.perform(post("/operacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idOperacaoCredito").value(1));
    }

    @Test
    void contratar_deveRetornarUnprocessableEntity_quandoContratoForRecusado() throws Exception {
        OperacaoCreditoRequestDTO requestDTO = new OperacaoCreditoRequestDTO(
                2L, new BigDecimal("3000"), Segmento.AGRO, "903C", "1234567890", null);

        when(operacaoCreditoService.contratar(any(OperacaoCreditoRequestDTO.class)))
                .thenThrow(new ContratoRecusadoException("Operações do segmento AGRO exigem areaBeneficiadaHa preenchido e maior que zero"));

        mockMvc.perform(post("/operacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.mensagem").value("Operações do segmento AGRO exigem areaBeneficiadaHa preenchido e maior que zero"));
    }

    @Test
    void buscarPorId_deveRetornarOperacao_quandoExistir() throws Exception {
        OperacaoCredito operacaoCredito = new OperacaoCredito();
        operacaoCredito.setIdOperacaoCredito(1L);

        OperacaoCreditoResponseDTO responseDTO = new OperacaoCreditoResponseDTO(
                1L, 10L, new BigDecimal("1000.00"), Segmento.PF,
                "101A", "1234567890", null, LocalDateTime.of(2026, 6, 15, 10, 0));

        when(operacaoCreditoService.buscarPorId(1L)).thenReturn(operacaoCredito);
        when(mapper.toResponseDTO(any(OperacaoCredito.class))).thenReturn(responseDTO);

        mockMvc.perform(get("/operacoes/{idOperacaoCredito}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOperacaoCredito").value(1))
                .andExpect(jsonPath("$.idAssociado").value(10))
                .andExpect(jsonPath("$.segmento").value("PF"))
                .andExpect(jsonPath("$.codigoProdutoCredito").value("101A"))
                .andExpect(jsonPath("$.codigoConta").value("1234567890"))
                .andExpect(jsonPath("$.dataHoraContratacao").exists());
    }

    @Test
    void buscarPorId_deveRetornarNotFound_quandoNaoExistir() throws Exception {
        when(operacaoCreditoService.buscarPorId(99L)).thenThrow(new OperacaoCreditoNaoEncontradaException(99L));

        mockMvc.perform(get("/operacoes/{idOperacaoCredito}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
