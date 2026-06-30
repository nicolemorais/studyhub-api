package br.ifsp.studyhub_api.dto;

import jakarta.validation.constraints.NotBlank;

public record TopicoRequestDTO(
    @NotBlank(message = "O título é obrigatório.")
    String titulo,

    @NotBlank(message = "A descrição do assunto do tópico é obrigatória.")
    String conteudo
) {}