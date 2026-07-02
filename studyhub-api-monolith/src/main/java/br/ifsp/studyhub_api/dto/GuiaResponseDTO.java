package br.ifsp.studyhub_api.dto;

import java.util.List;
import java.util.UUID;

import br.ifsp.studyhub_api.model.Guia;


public record GuiaResponseDTO(
        UUID id,
        String titulo,
        String descricao,
        UUID salaId,
        List<TopicoResponseDTO> topicos
    ) {
    public static GuiaResponseDTO fromEntity(Guia guia) {
        return new GuiaResponseDTO(
                guia.getId(),
                guia.getTitulo(),
                guia.getDescricao(),
                guia.getSalaId(),
                guia.getTopicos()
                        .stream()
                        .map(TopicoResponseDTO::fromEntity)
                        .toList());
    }
}
