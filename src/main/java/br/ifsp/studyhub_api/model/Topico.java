package br.ifsp.studyhub_api.model;

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

@Entity
@Table(name = "tb_topicos")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guia_id", nullable = false)
    private Guia guia;

    protected Topico() {
    }

    public Topico(String titulo, String conteudo, Guia guia) {
        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O título do tópico é obrigatório.");
        }
        if (conteudo == null || conteudo.isBlank()) {
            throw new BusinessException("A descrição do assunto do tópico é obrigatória.");
        }

        this.titulo = titulo;
        this.conteudo = conteudo;
        this.guia = guia;
    }

    public UUID getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public Guia getGuia() {
        return guia;
    }

    public void alterarConteudo(String novoTitulo, String novoConteudo) {
        if (novoTitulo != null) {
            if (novoTitulo.isBlank()) {
                throw new BusinessException("O título do tópico não pode ser alterado para um valor vazio.");
            }
            this.titulo = novoTitulo;
        }
        
        if (novoConteudo != null) {
            if (novoConteudo.isBlank()) {
                throw new BusinessException(
                        "Erro de Validação: A descrição do assunto não pode ser alterada para um valor vazio.");
            }
            this.conteudo = novoConteudo;
        }
    }
}
