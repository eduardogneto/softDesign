package br.com.eduardoneto.aquisicaocredito.domain.regra;

import br.com.eduardoneto.aquisicaocredito.model.Segmento;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RegraSegmentoRegistry {

    private final Map<Segmento, RegraSegmento> regras;
    private final RegraDefault regraDefault;

    public RegraSegmentoRegistry(List<RegraSegmento> todasAsRegras, RegraDefault regraDefault) {
        this.regraDefault = regraDefault;
        this.regras = todasAsRegras.stream()
                .filter(r -> r.segmento() != null)
                .collect(Collectors.toMap(RegraSegmento::segmento, Function.identity()));
    }

    public RegraSegmento obter(Segmento segmento) {
        return regras.getOrDefault(segmento, regraDefault);
    }
}
