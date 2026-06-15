package br.ifsp.studyhub_api.dto;

import br.ifsp.studyhub_api.model.Guia;
import java.util.List;
import java.util.UUID;

public record GuiaResponseDTO(
        UUID id,
        String titulo,
        List<TopicoResponseDTO> topicos,
        List<String> materiais) {
    public GuiaResponseDTO(Guia guia) {
        this(
            guia.getId(),
            guia.getTitulo(),
            guia.getTopicos().stream().map(TopicoResponseDTO::new).toList(),
            guia.getMateriais()
        );
    }
}