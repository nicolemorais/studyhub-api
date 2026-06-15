package br.ifsp.studyhub_api.model;

import br.ifsp.studyhub_api.exception.BusinessException;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

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
    private List<Usuario> alunos = new ArrayList<>();

    protected Sala() {
    }

    public Sala(String titulo, String descricao, Usuario criador) {
        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O nome da sala é obrigatório para sua criação.");
        }

        if (criador == null){
            throw new BusinessException("A sala precisa ter um professor responsável.");
        }

        this.titulo = titulo;
        this.descricao = descricao;
        this.criador = criador;
    }

    public void adicionarAluno(Usuario aluno) {
        if (!this.alunos.contains(aluno)) {
            this.alunos.add(aluno);
        }
    }

    public void removerAluno(Usuario aluno) {
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

    public List<Usuario> getAlunos() {
        return alunos;
    }

}
