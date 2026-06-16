package br.com.eduardoneto.aquisicaocredito.service;

import br.com.eduardoneto.aquisicaocredito.model.SocioBeneficiario;
import br.com.eduardoneto.aquisicaocredito.repository.SocioBeneficiarioRepository;
import org.springframework.stereotype.Service;

@Service
public class SocioBeneficiarioService {

    private final SocioBeneficiarioRepository socioBeneficiarioRepository;

    public SocioBeneficiarioService(SocioBeneficiarioRepository socioBeneficiarioRepository) {
        this.socioBeneficiarioRepository = socioBeneficiarioRepository;
    }

    public void registrar(Long idOperacaoCredito, Long idAssociado) {
        SocioBeneficiario socioBeneficiario = new SocioBeneficiario();
        socioBeneficiario.setIdOperacaoCredito(idOperacaoCredito);
        socioBeneficiario.setIdAssociado(idAssociado);
        socioBeneficiarioRepository.save(socioBeneficiario);
    }
}
