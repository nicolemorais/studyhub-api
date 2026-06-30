package br.com.studyhub.communication.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_messages")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private UUID salaId;

    @Column(name = "remetente_id", nullable = false)
    private UUID remetenteId;

    @Column(name = "conteudo_texto", columnDefinition = "TEXT")
    private String conteudoTexto;

    @Column(name = "url_anexo", length = 512)
    private String urlAnexo;

    @Column(name = "enviada_em", nullable = false, updatable = false)
    private LocalDateTime enviadaEm;

    protected Mensagem() {
    }

    public Mensagem(UUID salaId, UUID remetenteId, String conteudoTexto, String nomeArquivoAnexo, String urlAnexo) {
        if (salaId == null || remetenteId == null) {
            throw new IllegalArgumentException("Referências de sala e remetente são obrigatórias.");
        }

        if (conteudoTexto == null || conteudoTexto.trim().isEmpty()) {
            throw new IllegalArgumentException("O conteúdo da mensagem não pode estar vazio.");
        }

        validarSegurancaArquivo(nomeArquivoAnexo);

        this.salaId = salaId;
        this.remetenteId = remetenteId;
        this.conteudoTexto = conteudoTexto;
        this.urlAnexo = urlAnexo;
    }

    private void validarSegurancaArquivo(String nomeArquivo) {
        if (nomeArquivo != null && !nomeArquivo.isBlank()) {
            String arquivoLower = nomeArquivo.toLowerCase();
            if (arquivoLower.endsWith(".exe") || arquivoLower.endsWith(".bat")) {
                throw new SecurityException(
                        "Arquivos com extensões perigosas (.exe, .bat) são estritamente proibidos.");
            }
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getSalaId() {
        return salaId;
    }

    public UUID getRemetenteId() {
        return remetenteId;
    }

    public String getConteudoTexto() {
        return conteudoTexto;
    }

    public String getUrlAnexo() {
        return urlAnexo;
    }

    public LocalDateTime getEnviadaEm() {
        return enviadaEm;
    }

}
