package br.ifsp.studyhub_api.dto;

import java.util.UUID;

import br.ifsp.studyhub_api.model.Topico;

public record TopicoResponseDTO(UUID id, String titulo, String conteudo) {
    public TopicoResponseDTO(Topico topico) {
        this(topico.getId(), topico.getTitulo(), topico.getConteudo());
    }
}