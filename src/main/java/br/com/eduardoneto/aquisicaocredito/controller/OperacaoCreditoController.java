package br.com.eduardoneto.aquisicaocredito.controller;

import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoCriadaResponseDTO;
import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoRequestDTO;
import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoResponseDTO;
import br.com.eduardoneto.aquisicaocredito.mapper.OperacaoCreditoMapper;
import br.com.eduardoneto.aquisicaocredito.model.OperacaoCredito;
import br.com.eduardoneto.aquisicaocredito.service.OperacaoCreditoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Operações de Crédito", description = "API de aquisição de crédito")
@RestController
@RequestMapping("/operacoes")
public class OperacaoCreditoController {

    private final OperacaoCreditoService operacaoCreditoService;
    private final OperacaoCreditoMapper mapper;

    public OperacaoCreditoController(OperacaoCreditoService operacaoCreditoService,
                                      OperacaoCreditoMapper mapper) {
        this.operacaoCreditoService = operacaoCreditoService;
        this.mapper = mapper;
    }

    @Operation(summary = "Contratar operação de crédito")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Operação contratada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Contratação recusada"),
            @ApiResponse(responseCode = "503", description = "Serviço de produtos indisponível")
    })
    @PostMapping
    public ResponseEntity<OperacaoCreditoCriadaResponseDTO> contratar(@RequestBody OperacaoCreditoRequestDTO requestDTO) {
        OperacaoCredito operacaoCredito = operacaoCreditoService.contratar(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new OperacaoCreditoCriadaResponseDTO(operacaoCredito.getIdOperacaoCredito()));
    }

    @Operation(summary = "Consultar operação de crédito por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação encontrada"),
            @ApiResponse(responseCode = "404", description = "Operação não encontrada")
    })
    @GetMapping("/{idOperacaoCredito}")
    public ResponseEntity<OperacaoCreditoResponseDTO> buscarPorId(@PathVariable Long idOperacaoCredito) {
        OperacaoCredito operacaoCredito = operacaoCreditoService.buscarPorId(idOperacaoCredito);
        return ResponseEntity.ok(mapper.toResponseDTO(operacaoCredito));
    }
}
