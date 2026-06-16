package br.com.eduardoneto.aquisicaocredito.repository;

import br.com.eduardoneto.aquisicaocredito.model.SocioBeneficiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocioBeneficiarioRepository extends JpaRepository<SocioBeneficiario, Long> {
}
