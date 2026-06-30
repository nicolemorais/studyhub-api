package br.ifsp.studyhub_api.dto;

public record TokenResponseDTO(
    String token,
    String tipo
) {
    public TokenResponseDTO(String token) {
        this(token, "Bearer");
    }
}
