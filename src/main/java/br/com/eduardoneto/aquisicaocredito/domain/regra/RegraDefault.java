package br.com.eduardoneto.aquisicaocredito.domain.regra;

import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoRequestDTO;
import br.com.eduardoneto.aquisicaocredito.model.OperacaoCredito;
import br.com.eduardoneto.aquisicaocredito.model.Segmento;
import org.springframework.stereotype.Component;

@Component
public class RegraDefault implements RegraSegmento {

    @Override
    public Segmento segmento() {
        return null;
    }

    @Override
    public void validar(OperacaoCreditoRequestDTO requestDTO) {
    }

    @Override
    public void posContratar(OperacaoCredito operacaoCredito) {
    }
}
