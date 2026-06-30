package br.com.studyhub.communication.interfaces.dto;
import java.util.UUID;

public record MensagemRequestDTO(UUID remetenteId, String conteudoTexto, String nomeArquivo, String urlAnexo) {}