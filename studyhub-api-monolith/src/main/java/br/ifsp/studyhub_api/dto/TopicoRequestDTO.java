package br.ifsp.studyhub_api.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicoRequestDTO(
    @NotBlank(message = "O título do tópico é obrigatório.")
    String titulo,
    
    String conteudo,
    
    @NotNull(message = "A ordem de exibição deve ser definida.")
    Integer ordemExibicao,
    
    @NotNull(message = "O ID da guia é obrigatório.")
    UUID guiaId
) {}