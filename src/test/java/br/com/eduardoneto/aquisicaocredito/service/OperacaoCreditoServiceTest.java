package br.com.eduardoneto.aquisicaocredito.service;

import br.com.eduardoneto.aquisicaocredito.domain.regra.RegraSegmento;
import br.com.eduardoneto.aquisicaocredito.domain.regra.RegraSegmentoRegistry;
import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoRequestDTO;
import br.com.eduardoneto.aquisicaocredito.exception.ContratoRecusadoException;
import br.com.eduardoneto.aquisicaocredito.exception.OperacaoCreditoNaoEncontradaException;
import br.com.eduardoneto.aquisicaocredito.mapper.OperacaoCreditoMapper;
import br.com.eduardoneto.aquisicaocredito.model.OperacaoCredito;
import br.com.eduardoneto.aquisicaocredito.model.Segmento;
import br.com.eduardoneto.aquisicaocredito.repository.OperacaoCreditoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperacaoCreditoServiceTest {

    @Mock
    private OperacaoCreditoRepository operacaoCreditoRepository;

    @Mock
    private ProdutoCreditoGateway produtoCreditoGateway;

    @Mock
    private OperacaoCreditoMapper mapper;

    @Mock
    private RegraSegmentoRegistry regraSegmentoRegistry;

    @Mock
    private RegraSegmento regraSegmento;

    @InjectMocks
    private OperacaoCreditoService operacaoCreditoService;

    @Test
    void contratar_devePersistirOperacao_quandoSegmentoPF() {
        OperacaoCreditoRequestDTO requestDTO = new OperacaoCreditoRequestDTO(
                1L, new BigDecimal("1000"), Segmento.PF, "101A", "1234567890", null);

        when(regraSegmentoRegistry.obter(Segmento.PF)).thenReturn(regraSegmento);
        when(produtoCreditoGateway.permiteContratacao("101A", Segmento.PF, new BigDecimal("1000")))
                .thenReturn(true);
        when(mapper.toEntity(requestDTO)).thenReturn(new OperacaoCredito());
        when(operacaoCreditoRepository.save(any(OperacaoCredito.class)))
                .thenAnswer(invocation -> {
                    OperacaoCredito operacao = invocation.getArgument(0);
                    operacao.setIdOperacaoCredito(1L);
                    return operacao;
                });

        OperacaoCredito resultado = operacaoCreditoService.contratar(requestDTO);

        assertThat(resultado.getIdOperacaoCredito()).isEqualTo(1L);
        assertThat(resultado.getDataHoraContratacao()).isNotNull();
        verify(regraSegmento).posContratar(resultado);
    }

    @Test
    void contratar_devePersistirSocioBeneficiario_quandoSegmentoPJ() {
        OperacaoCreditoRequestDTO requestDTO = new OperacaoCreditoRequestDTO(
                2L, new BigDecimal("10000"), Segmento.PJ, "202B", "1234567890", null);

        when(regraSegmentoRegistry.obter(Segmento.PJ)).thenReturn(regraSegmento);
        when(produtoCreditoGateway.permiteContratacao("202B", Segmento.PJ, new BigDecimal("10000")))
                .thenReturn(true);
        when(mapper.toEntity(requestDTO)).thenReturn(new OperacaoCredito());
        when(operacaoCreditoRepository.save(any(OperacaoCredito.class)))
                .thenAnswer(invocation -> {
                    OperacaoCredito operacao = invocation.getArgument(0);
                    operacao.setIdOperacaoCredito(10L);
                    return operacao;
                });

        operacaoCreditoService.contratar(requestDTO);

        ArgumentCaptor<OperacaoCredito> captor = ArgumentCaptor.forClass(OperacaoCredito.class);
        verify(regraSegmento).posContratar(captor.capture());
        assertThat(captor.getValue().getIdOperacaoCredito()).isEqualTo(10L);
    }

    @Test
    void contratar_devePersistir_quandoSegmentoAgroComAreaBeneficiadaValida() {
        OperacaoCreditoRequestDTO requestDTO = new OperacaoCreditoRequestDTO(
                3L, new BigDecimal("3000"), Segmento.AGRO, "903C", "1234567890", new BigDecimal("10.5"));

        when(regraSegmentoRegistry.obter(Segmento.AGRO)).thenReturn(regraSegmento);
        when(produtoCreditoGateway.permiteContratacao("903C", Segmento.AGRO, new BigDecimal("3000")))
                .thenReturn(true);
        when(mapper.toEntity(requestDTO)).thenReturn(new OperacaoCredito());
        when(operacaoCreditoRepository.save(any(OperacaoCredito.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OperacaoCredito resultado = operacaoCreditoService.contratar(requestDTO);

        assertThat(resultado.getDataHoraContratacao()).isNotNull();
        verify(regraSegmento).posContratar(resultado);
    }

    @Test
    void contratar_deveRecusar_quandoAgroSemAreaBeneficiada() {
        OperacaoCreditoRequestDTO requestDTO = new OperacaoCreditoRequestDTO(
                4L, new BigDecimal("3000"), Segmento.AGRO, "903C", "1234567890", null);

        when(regraSegmentoRegistry.obter(Segmento.AGRO)).thenReturn(regraSegmento);
        doThrow(new ContratoRecusadoException("Operações do segmento AGRO exigem areaBeneficiadaHa preenchido e maior que zero"))
                .when(regraSegmento).validar(requestDTO);

        assertThatThrownBy(() -> operacaoCreditoService.contratar(requestDTO))
                .isInstanceOf(ContratoRecusadoException.class);

        verifyNoInteractions(produtoCreditoGateway, operacaoCreditoRepository, mapper);
        verify(regraSegmento, never()).posContratar(any());
    }

    @Test
    void contratar_deveRecusar_quandoAgroComAreaBeneficiadaIgualAZero() {
        OperacaoCreditoRequestDTO requestDTO = new OperacaoCreditoRequestDTO(
                5L, new BigDecimal("3000"), Segmento.AGRO, "903C", "1234567890", BigDecimal.ZERO);

        when(regraSegmentoRegistry.obter(Segmento.AGRO)).thenReturn(regraSegmento);
        doThrow(new ContratoRecusadoException("Operações do segmento AGRO exigem areaBeneficiadaHa preenchido e maior que zero"))
                .when(regraSegmento).validar(requestDTO);

        assertThatThrownBy(() -> operacaoCreditoService.contratar(requestDTO))
                .isInstanceOf(ContratoRecusadoException.class);

        verifyNoInteractions(produtoCreditoGateway, operacaoCreditoRepository, mapper);
        verify(regraSegmento, never()).posContratar(any());
    }

    @Test
    void contratar_deveRecusar_quandoAgroComAreaBeneficiadaNegativa() {
        OperacaoCreditoRequestDTO requestDTO = new OperacaoCreditoRequestDTO(
                6L, new BigDecimal("3000"), Segmento.AGRO, "903C", "1234567890", new BigDecimal("-1"));

        when(regraSegmentoRegistry.obter(Segmento.AGRO)).thenReturn(regraSegmento);
        doThrow(new ContratoRecusadoException("Operações do segmento AGRO exigem areaBeneficiadaHa preenchido e maior que zero"))
                .when(regraSegmento).validar(requestDTO);

        assertThatThrownBy(() -> operacaoCreditoService.contratar(requestDTO))
                .isInstanceOf(ContratoRecusadoException.class);

        verifyNoInteractions(produtoCreditoGateway, operacaoCreditoRepository, mapper);
        verify(regraSegmento, never()).posContratar(any());
    }

    @Test
    void contratar_deveRecusar_quandoProdutoNaoPermiteContratacao() {
        OperacaoCreditoRequestDTO requestDTO = new OperacaoCreditoRequestDTO(
                7L, new BigDecimal("500"), Segmento.PF, "101A", "1234567890", null);

        when(regraSegmentoRegistry.obter(Segmento.PF)).thenReturn(regraSegmento);
        when(produtoCreditoGateway.permiteContratacao("101A", Segmento.PF, new BigDecimal("500")))
                .thenReturn(false);

        assertThatThrownBy(() -> operacaoCreditoService.contratar(requestDTO))
                .isInstanceOf(ContratoRecusadoException.class);

        verify(mapper, never()).toEntity(any());
        verify(operacaoCreditoRepository, never()).save(any());
        verify(regraSegmento, never()).posContratar(any());
    }

    @Test
    void buscarPorId_deveRetornarOperacao_quandoExistir() {
        OperacaoCredito operacao = new OperacaoCredito();
        operacao.setIdOperacaoCredito(1L);

        when(operacaoCreditoRepository.findById(1L)).thenReturn(Optional.of(operacao));

        OperacaoCredito resultado = operacaoCreditoService.buscarPorId(1L);

        assertThat(resultado).isSameAs(operacao);
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoNaoEncontrada() {
        when(operacaoCreditoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> operacaoCreditoService.buscarPorId(99L))
                .isInstanceOf(OperacaoCreditoNaoEncontradaException.class);
    }
}
