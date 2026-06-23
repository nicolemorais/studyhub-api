package br.com.studyhub.auth.interfaces.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorMessageDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        List<String> details) {
    public ErrorMessageDTO(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message, List.of());
    }

    public ErrorMessageDTO(int status, String error, String message, List<String> details) {
        this(LocalDateTime.now(), status, error, message, details);
    }
}