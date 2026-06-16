package br.ifsp.studyhub_api.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record TopicoPutRequestDTO(
    UUID id,
    
    @NotBlank(message = "O título do tópico não pode ser alterado para um valor vazio.")
    String titulo,

    @NotBlank(message = "A descrição do assunto não pode ser alterada para um valor vazio.")
    String descricao
) {}