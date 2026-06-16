package br.com.eduardoneto.aquisicaocredito.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ContratoRecusadoException.class)
    public ResponseEntity<ErroResponseDTO> tratarContratoRecusado(ContratoRecusadoException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErroResponseDTO.de(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage()));
    }

    @ExceptionHandler(OperacaoCreditoNaoEncontradaException.class)
    public ResponseEntity<ErroResponseDTO> tratarOperacaoNaoEncontrada(OperacaoCreditoNaoEncontradaException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErroResponseDTO.de(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(ServicoProdutoCreditoIndisponivelException.class)
    public ResponseEntity<ErroResponseDTO> tratarServicoIndisponivel(ServicoProdutoCreditoIndisponivelException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ErroResponseDTO.de(HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getMessage()));
    }
}
