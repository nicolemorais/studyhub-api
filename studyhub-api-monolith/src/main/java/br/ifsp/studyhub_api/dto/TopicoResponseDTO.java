package br.ifsp.studyhub_api.dto;

import java.util.List;
import java.util.UUID;

import br.ifsp.studyhub_api.model.Topico;

public record TopicoResponseDTO(
        UUID id,
        String titulo,
        String conteudo,
        Integer ordemExibicao,
        UUID guiaId,
        List<MaterialResponseDTO> materiais
    ) {
    public static TopicoResponseDTO fromEntity(Topico topico) {
        return new TopicoResponseDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getConteudo(),
                topico.getOrdemExibicao(),
                topico.getGuia().getId(),
                topico.getMateriais()
                        .stream()
                        .map(MaterialResponseDTO::fromEntity)
                        .toList());
    }
}