package br.com.eduardoneto.aquisicaocredito.mapper;

import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoRequestDTO;
import br.com.eduardoneto.aquisicaocredito.dto.OperacaoCreditoResponseDTO;
import br.com.eduardoneto.aquisicaocredito.model.OperacaoCredito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OperacaoCreditoMapper {

    @Mapping(target = "idOperacaoCredito", ignore = true)
    @Mapping(target = "dataHoraContratacao", ignore = true)
    OperacaoCredito toEntity(OperacaoCreditoRequestDTO requestDTO);

    OperacaoCreditoResponseDTO toResponseDTO(OperacaoCredito operacaoCredito);
}
