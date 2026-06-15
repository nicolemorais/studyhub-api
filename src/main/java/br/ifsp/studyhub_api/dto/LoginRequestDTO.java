package br.ifsp.studyhub_api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "E-mail ou senha inválidos.") String email,

        @NotBlank(message = "E-mail ou senha inválidos.") String senha
){}