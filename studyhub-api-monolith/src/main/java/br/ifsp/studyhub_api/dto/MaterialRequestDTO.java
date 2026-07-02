package br.ifsp.studyhub_api.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MaterialRequestDTO(
        @NotBlank(message = "O título do material é obrigatório.") String titulo,

        // Pode ser enviado vazio, mas se for preenchido a Entidade barrará .exe e .bat!
        String urlArquivo,

        @NotNull(message = "O ID do tópico é obrigatório.") UUID topicoId
    ) {}
