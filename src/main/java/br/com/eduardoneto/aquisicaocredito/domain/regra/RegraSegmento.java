package br.com.eduardoneto.aquisicaocredito.domain.regra;

import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoRequestDTO;
import br.com.eduardoneto.aquisicaocredito.model.OperacaoCredito;
import br.com.eduardoneto.aquisicaocredito.model.Segmento;

public interface RegraSegmento {

    Segmento segmento();

    void validar(OperacaoCreditoRequestDTO requestDTO);

    void posContratar(OperacaoCredito operacaoCredito);
}
