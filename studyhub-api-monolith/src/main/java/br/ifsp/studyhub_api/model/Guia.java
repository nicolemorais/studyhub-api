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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_guias")
public class Guia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Column(name = "sala_id", nullable = false)
    private UUID salaId;

    @OneToMany(mappedBy = "guia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Topico> topicos = new ArrayList<>();

    protected Guia() {
    }

    public Guia(String titulo, String descricao, UUID salaId) {
        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O título do guia de estudos é obrigatório.");
        }
        if (salaId == null) {
            throw new BusinessException("O guia de estudos precisa estar vinculado a uma sala válida.");
        }
        this.titulo = titulo;
        this.descricao = descricao;
        this.salaId = salaId;
    }

    public void atualizar(String titulo, String descricao) {

        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O título da guia é obrigatório.");
        }

        this.titulo = titulo;
        this.descricao = descricao;
    }

    public void adicionarTopico(Topico topico) {

        if (topico == null) {
            throw new BusinessException("O tópico não pode ser nulo.");
        }

        topicos.add(topico);
    }

    public void removerTopico(UUID topicoId) {

        topicos.removeIf(t -> t.getId().equals(topicoId));
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

    public UUID getSalaId() {
        return salaId;
    }

    public List<Topico> getTopicos() {
        return topicos;
    }
}