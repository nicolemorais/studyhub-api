package br.ifsp.studyhub_api.dto;

import java.util.UUID;

public record AlunoResponseDTO(
    UUID id,
    String nome,
    String email
) {}