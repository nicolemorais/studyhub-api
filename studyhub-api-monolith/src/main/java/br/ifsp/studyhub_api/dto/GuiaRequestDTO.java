package br.ifsp.studyhub_api.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GuiaRequestDTO(
        @NotBlank(message = "O título é obrigatório.") 
        String titulo,

        @Size(max = 255, message = "A descrição não deve exceder 255 caracteres.") 
        String descricao,

        @NotNull(message = "O ID da sala é obrigatório para criar um guia.") 
        UUID salaId
    ) {}
