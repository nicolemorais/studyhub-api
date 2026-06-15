package br.ifsp.studyhub_api.model;

import br.ifsp.studyhub_api.exception.BusinessException;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tb_topicos")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guia_id", nullable = false)
    private Guia guia;

    protected Topico() {
    }

    public Topico(String titulo, String descricao, Guia guia) {
        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O título do tópico é obrigatório.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new BusinessException("A descrição do assunto do tópico é obrigatória.");
        }

        this.titulo = titulo;
        this.descricao = descricao;
        this.guia = guia;
    }

    public UUID getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Guia getGuia() {
        return guia;
    }

    public void alterarConteudo(String novoTitulo, String novaDescricao) {
        if (novoTitulo != null) {
            if (novoTitulo.isBlank()) {
                throw new BusinessException("O título do tópico não pode ser alterado para um valor vazio.");
            }
            this.titulo = novoTitulo;
        }
        
        if (novaDescricao != null) {
            if (novaDescricao.isBlank()) {
                throw new BusinessException(
                        "Erro de Validação: A descrição do assunto não pode ser alterada para um valor vazio.");
            }
            this.descricao = novaDescricao;
        }
    }
}
