package br.com.studyhub.communication.interfaces.dto;

import java.util.List;

public record PaginaRequestDTO<T>(
        List<T> content,
        int paginaCorrente,
        int totalPaginas,
        long totalElementos
) {}