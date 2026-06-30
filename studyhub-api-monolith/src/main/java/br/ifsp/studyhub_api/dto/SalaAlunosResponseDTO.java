package br.ifsp.studyhub_api.dto;

import java.util.List;
import java.util.UUID;

public record SalaAlunosResponseDTO(
        UUID id,
        String titulo,
        String descricao,
        List<AlunoResponseDTO> alunos) {
}
