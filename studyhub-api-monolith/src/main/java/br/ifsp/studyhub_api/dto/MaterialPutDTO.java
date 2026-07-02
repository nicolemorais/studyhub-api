package br.ifsp.studyhub_api.dto;

import jakarta.validation.constraints.NotBlank;

public record MaterialPutDTO(
    @NotBlank(message = "O título do material não pode ficar vazio.")
    String titulo,
    
    String urlArquivo
) {}