package br.ifsp.studyhub_api.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record GuiaPutRequestDTO(
    @NotBlank(message = "O título da guia não pode ser alterado para um valor vazio.")
    String titulo,

    @NotEmpty(message = "A guia deve conter pelo menos 1 tópico ativo.") 
    List<TopicoPutDTO> topicos,

    List<String> materiais
) {}
