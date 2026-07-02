package br.ifsp.studyhub_api.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import br.ifsp.studyhub_api.exception.BusinessException;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    @Column(name = "professor_id", nullable = false)
    private UUID professorId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tb_salas_alunos", joinColumns = @JoinColumn(name = "sala_id"))
    @Column(name = "usuario_id")
    private Set<UUID> alunosIds = new HashSet<>();

    protected Sala() {
    }

    public Sala(String titulo, String descricao, UUID professorId) {
        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O nome da sala é obrigatório para sua criação.");
        }

        if (professorId == null) {
            throw new BusinessException("A sala precisa ter um professor responsável.");
        }

        this.titulo = titulo;
        this.descricao = descricao;
        this.professorId = professorId;
    }

    public void adicionarAluno(UUID alunoId) {
        if (alunoId != null) {
            this.alunosIds.add(alunoId);
        }
    }

    public void removerAluno(UUID alunoId) {
        this.alunosIds.remove(alunoId);
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

    public UUID getProfessorId() {
        return professorId;
    }

    public Set<UUID> getAlunosIds() {
        return alunosIds;
    }
}
