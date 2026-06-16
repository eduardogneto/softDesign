package br.com.eduardoneto.aquisicaocredito.exception;

public class OperacaoCreditoNaoEncontradaException extends RuntimeException {

    public OperacaoCreditoNaoEncontradaException(Long idOperacaoCredito) {
        super("Operação de crédito não encontrada para o id " + idOperacaoCredito);
    }
}
