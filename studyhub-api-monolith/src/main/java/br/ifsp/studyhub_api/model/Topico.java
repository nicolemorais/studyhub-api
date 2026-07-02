package br.ifsp.studyhub_api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.ifsp.studyhub_api.exception.BusinessException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guia_id", nullable = false)
    private Guia guia;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materiais = new ArrayList<>();

    protected Topico() {
    }

    public Topico(String titulo, String conteudo, Integer ordemExibicao, Guia guia) {
        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O título do tópico é obrigatório.");
        }

        this.titulo = titulo;
        this.conteudo = conteudo;
        this.ordemExibicao = ordemExibicao;
        this.guia = guia;
    }

    public void atualizar(String titulo,
            String conteudo,
            Integer ordemExibicao) {

        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O título do tópico é obrigatório.");
        }

        this.titulo = titulo;
        this.conteudo = conteudo;
        this.ordemExibicao = ordemExibicao;
    }

    public void adicionarMaterial(Material material) {

        if (material == null) {
            throw new BusinessException("O material não pode ser nulo.");
        }

        materiais.add(material);
    }

    public void removerMaterial(UUID materialId) {

        materiais.removeIf(m -> m.getId().equals(materialId));
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

    public Integer getOrdemExibicao() {
        return ordemExibicao;
    }

    public Guia getGuia() {
        return guia;
    }

    public List<Material> getMateriais() {
        return materiais;
    }
}
