package br.com.eduardoneto.aquisicaocredito.service;

import br.com.eduardoneto.aquisicaocredito.domain.regra.RegraSegmento;
import br.com.eduardoneto.aquisicaocredito.domain.regra.RegraSegmentoRegistry;
import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoRequestDTO;
import br.com.eduardoneto.aquisicaocredito.exception.ContratoRecusadoException;
import br.com.eduardoneto.aquisicaocredito.exception.OperacaoCreditoNaoEncontradaException;
import br.com.eduardoneto.aquisicaocredito.mapper.OperacaoCreditoMapper;
import br.com.eduardoneto.aquisicaocredito.model.OperacaoCredito;
import br.com.eduardoneto.aquisicaocredito.repository.OperacaoCreditoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OperacaoCreditoService {

    private final OperacaoCreditoRepository operacaoCreditoRepository;
    private final ProdutoCreditoGateway produtoCreditoGateway;
    private final OperacaoCreditoMapper mapper;
    private final RegraSegmentoRegistry regraSegmentoRegistry;

    public OperacaoCreditoService(OperacaoCreditoRepository operacaoCreditoRepository,
                                   ProdutoCreditoGateway produtoCreditoGateway,
                                   OperacaoCreditoMapper mapper,
                                   RegraSegmentoRegistry regraSegmentoRegistry) {
        this.operacaoCreditoRepository = operacaoCreditoRepository;
        this.produtoCreditoGateway = produtoCreditoGateway;
        this.mapper = mapper;
        this.regraSegmentoRegistry = regraSegmentoRegistry;
    }

    @Transactional
    public OperacaoCredito contratar(OperacaoCreditoRequestDTO requestDTO) {
        RegraSegmento regra = regraSegmentoRegistry.obter(requestDTO.segmento());
        regra.validar(requestDTO);

        boolean permiteContratacao = produtoCreditoGateway.permiteContratacao(
                requestDTO.codigoProdutoCredito(), requestDTO.segmento(), requestDTO.valorOperacao());

        if (!permiteContratacao) {
            throw new ContratoRecusadoException(
                    "O produto de crédito %s não permite a contratação para o segmento %s no valor informado"
                            .formatted(requestDTO.codigoProdutoCredito(), requestDTO.segmento()));
        }

        OperacaoCredito operacaoCredito = mapper.toEntity(requestDTO);
        operacaoCredito.setDataHoraContratacao(LocalDateTime.now());

        operacaoCredito = operacaoCreditoRepository.save(operacaoCredito);

        regra.posContratar(operacaoCredito);

        return operacaoCredito;
    }

    public OperacaoCredito buscarPorId(Long idOperacaoCredito) {
        return operacaoCreditoRepository.findById(idOperacaoCredito)
                .orElseThrow(() -> new OperacaoCreditoNaoEncontradaException(idOperacaoCredito));
    }
}
