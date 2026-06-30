package br.com.studyhub.communication.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record MensagemEnviadaEvent(
        UUID mensagemId,
        UUID salaId,
        UUID remetenteId,
        LocalDateTime enviadaEm) {}
