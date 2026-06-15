package br.ifsp.studyhub_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record GuiaRequestDTO(
    @NotBlank(message = "O título é obrigatório.")
    String titulo,

    @NotEmpty(message = "A guia deve conter pelo menos 1 tópico.") 
    List<TopicoRequestDTO> topicos,

    List<String> materiais
) {}