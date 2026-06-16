package br.com.eduardoneto.aquisicaocredito.domain.regra;

import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoRequestDTO;
import br.com.eduardoneto.aquisicaocredito.exception.ContratoRecusadoException;
import br.com.eduardoneto.aquisicaocredito.model.OperacaoCredito;
import br.com.eduardoneto.aquisicaocredito.model.Segmento;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RegraAgro implements RegraSegmento {

    @Override
    public Segmento segmento() {
        return Segmento.AGRO;
    }

    @Override
    public void validar(OperacaoCreditoRequestDTO requestDTO) {
        BigDecimal areaBeneficiadaHa = requestDTO.areaBeneficiadaHa();
        if (areaBeneficiadaHa == null || areaBeneficiadaHa.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ContratoRecusadoException(
                    "Operações do segmento AGRO exigem areaBeneficiadaHa preenchido e maior que zero");
        }
    }

    @Override
    public void posContratar(OperacaoCredito operacaoCredito) {
    }
}
