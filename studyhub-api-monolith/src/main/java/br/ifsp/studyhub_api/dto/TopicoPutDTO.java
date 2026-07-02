package br.ifsp.studyhub_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicoPutDTO(
    @NotBlank(message = "O título do tópico não pode ficar vazio.")
    String titulo,
    
    String conteudo,
    
    @NotNull(message = "A ordem de exibição não pode ser nula.")
    Integer ordemExibicao
) {}