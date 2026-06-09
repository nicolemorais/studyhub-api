package br.ifsp.studyhub_api.dto;

import br.ifsp.studyhub_api.model.Sala;
import java.util.UUID;

public record SalaResponseDTO(UUID id, String titulo, String descricao) {

    public SalaResponseDTO(Sala entity) {
        this(entity.getId(), entity.getTitulo(), entity.getDescricao());
    }
}