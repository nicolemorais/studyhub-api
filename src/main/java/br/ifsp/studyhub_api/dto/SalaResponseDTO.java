package br.ifsp.studyhub_api.dto;

import java.util.UUID;

import br.ifsp.studyhub_api.model.Sala;

public record SalaResponseDTO(UUID id, String titulo, String descricao) {

    public SalaResponseDTO(Sala entity) {
        this(entity.getId(), entity.getTitulo(), entity.getDescricao());
    }
}