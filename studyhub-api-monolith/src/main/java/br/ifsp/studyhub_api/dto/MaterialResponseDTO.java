package br.ifsp.studyhub_api.dto;

import java.util.UUID;

import br.ifsp.studyhub_api.model.Material;

public record MaterialResponseDTO(
        UUID id,
        String titulo,
        String urlArquivo,
        UUID topicoId
    ) {
    public static MaterialResponseDTO fromEntity(Material material) {
        return new MaterialResponseDTO(
                material.getId(),
                material.getTitulo(),
                material.getUrlArquivo(),
                material.getTopico().getId());
    }
}