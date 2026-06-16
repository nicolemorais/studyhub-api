package br.ifsp.studyhub_api.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record GuiaRequestDTO(
    @NotBlank(message = "O título é obrigatório.")
    String titulo,

    @NotEmpty(message = "A guia deve conter pelo menos 1 tópico.") 
    List<TopicoRequestDTO> topicos,

    List<String> materiais
) {}