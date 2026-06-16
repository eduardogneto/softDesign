package br.com.eduardoneto.aquisicaocredito.domain.regra;

import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoRequestDTO;
import br.com.eduardoneto.aquisicaocredito.model.OperacaoCredito;
import br.com.eduardoneto.aquisicaocredito.model.Segmento;
import br.com.eduardoneto.aquisicaocredito.service.SocioBeneficiarioService;
import org.springframework.stereotype.Component;

@Component
public class RegraPJ implements RegraSegmento {

    private final SocioBeneficiarioService socioBeneficiarioService;

    public RegraPJ(SocioBeneficiarioService socioBeneficiarioService) {
        this.socioBeneficiarioService = socioBeneficiarioService;
    }

    @Override
    public Segmento segmento() {
        return Segmento.PJ;
    }

    @Override
    public void validar(OperacaoCreditoRequestDTO requestDTO) {
    }

    @Override
    public void posContratar(OperacaoCredito operacaoCredito) {
        socioBeneficiarioService.registrar(
                operacaoCredito.getIdOperacaoCredito(),
                operacaoCredito.getIdAssociado());
    }
}
