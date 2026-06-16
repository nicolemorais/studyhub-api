package br.ifsp.studyhub_api.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record GuiaRequestDTO(
    @NotBlank(message = "O título é obrigatório.")
    String titulo,
    
    @Size(max = 255, message = "A descrição não deve exceder 255 caracteres.")
    String descricao,

    @NotEmpty(message = "A guia deve conter pelo menos 1 tópico.") 
    List<TopicoRequestDTO> topicos,

    List<String> materiais
) {}