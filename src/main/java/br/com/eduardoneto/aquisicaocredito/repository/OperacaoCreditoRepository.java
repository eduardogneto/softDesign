package br.com.eduardoneto.aquisicaocredito.repository;

import br.com.eduardoneto.aquisicaocredito.model.OperacaoCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperacaoCreditoRepository extends JpaRepository<OperacaoCredito, Long> {
}
