package br.ifsp.studyhub_api.model;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import br.ifsp.studyhub_api.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_salas")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador_id", nullable = false)
    private Usuario criador;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tb_salas_alunos", joinColumns = @JoinColumn(name = "sala_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> alunos = new LinkedHashSet<>();

    protected Sala() {
    }

    public Sala(String titulo, String descricao, Usuario criador) {
        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O nome da sala é obrigatório para sua criação.");
        }

        if (criador == null) {
            throw new BusinessException("A sala precisa ter um professor responsável.");
        }

        this.titulo = titulo;
        this.descricao = descricao;
        this.criador = criador;
    }

    public void adicionarAluno(Usuario aluno) {
        if (aluno == null)
            return;

        this.alunos.add(aluno);
    }

    public void removerAluno(Usuario aluno) {
        if (aluno == null)
            return;
        this.alunos.remove(aluno);
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

    public Set<Usuario> getAlunos() {
        return this.alunos;
    }
}
