package br.ifsp.studyhub_api.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record TopicoPutRequestDTO(
    UUID id,
    
    @NotBlank(message = "O título do tópico não pode ser alterado para um valor vazio.")
    String titulo,

    @NotBlank(message = "A descrição do assunto não pode ser alterada para um valor vazio.")
    String descricao
) {}