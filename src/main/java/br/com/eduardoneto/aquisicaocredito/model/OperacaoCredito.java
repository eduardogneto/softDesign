package br.com.eduardoneto.aquisicaocredito.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "titulo_credito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operacao_credito")
    private Long idOperacaoCredito;

    @Column(name = "id_associado", nullable = false)
    private Long idAssociado;

    @Column(name = "valor_operacao", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorOperacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "segmento", nullable = false, length = 4)
    private Segmento segmento;

    @Column(name = "codigo_produto_credito", nullable = false, length = 4)
    private String codigoProdutoCredito;

    @Column(name = "codigo_conta", nullable = false, length = 10)
    private String codigoConta;

    @Column(name = "area_beneficiada_ha", precision = 19, scale = 2)
    private BigDecimal areaBeneficiadaHa;

    @Column(name = "data_hora_contratacao", nullable = false)
    private LocalDateTime dataHoraContratacao;
}
