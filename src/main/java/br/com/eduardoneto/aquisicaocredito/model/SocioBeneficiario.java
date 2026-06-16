package br.com.eduardoneto.aquisicaocredito.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "socio_beneficiario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocioBeneficiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_operacao_credito", nullable = false)
    private Long idOperacaoCredito;

    @Column(name = "id_associado", nullable = false)
    private Long idAssociado;
}
