package br.ifsp.studyhub_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record GuiaPutRequestDTO(
    @NotBlank(message = "O título da guia não pode ser alterado para um valor vazio.")
    String titulo,

    @NotEmpty(message = "A guia deve conter pelo menos 1 tópico ativo.") 
    List<TopicoPutRequestDTO> topicos,

    List<String> materiais
) {}
