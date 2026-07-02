package br.ifsp.studyhub_api.model;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import br.ifsp.studyhub_api.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "tb_materiais")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(name = "url_arquivo")
    private String urlArquivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id", nullable = false)
    private Topico topico;

    @Transient
    private final List<String> EXTENSOES_PERMITIDAS = Arrays.asList(
            ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".zip");

    protected Material() {
    }

    public Material(String titulo, String urlArquivo, Topico topico) {
        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O título do material é obrigatório.");
        }

        validarSegurancaArquivo(urlArquivo);

        this.titulo = titulo;
        this.urlArquivo = urlArquivo;
        this.topico = topico;
    }

    /**
     * Validação Binária Ativa
     */
    private void validarSegurancaArquivo(String url) {
        if (url == null || url.isBlank()) {
            return; // Pode ser apenas um material textual sem ficheiro anexado
        }

        String urlLower = url.toLowerCase();

        // Regra de segurança para bloqueio
        if (urlLower.endsWith(".exe") || urlLower.endsWith(".bat") || urlLower.endsWith(".sh")) {
            throw new BusinessException("Tipos de ficheiros executáveis não são permitidos.");
        }

        boolean formatoValido = EXTENSOES_PERMITIDAS.stream().anyMatch(urlLower::endsWith);
        if (!formatoValido) {
            throw new BusinessException("Formato inválido. Apenas .pdf, .doc(x), .xls(x) e .zip são permitidos.");
        }
    }

    public void atualizar(String titulo, String urlArquivo) {

        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O título do material é obrigatório.");
        }

        validarSegurancaArquivo(urlArquivo);

        this.titulo = titulo;
        this.urlArquivo = urlArquivo;
    }

    public UUID getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrlArquivo() {
        return urlArquivo;
    }

    public Topico getTopico() {
        return topico;
    }

    public List<String> getEXTENSOES_PERMITIDAS() {
        return EXTENSOES_PERMITIDAS;
    }
}