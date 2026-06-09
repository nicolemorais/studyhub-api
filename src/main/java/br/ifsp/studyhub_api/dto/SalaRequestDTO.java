package br.ifsp.studyhub_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SalaRequestDTO(
    @NotBlank(message = "O preenchimento do campo 'título' é obrigatório.")
    @Size(max = 180, message = "O título não pode ultrapassar 180 caracteres..")
    String titulo,

    @Size(max = 255, message = "A descrição não pode ultrapassar 255 caracteres.")
    String descricao
) {}