package br.ifsp.studyhub_api.model;

import br.ifsp.studyhub_api.exception.BusinessException;
import jakarta.persistence.*;

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

    protected Sala() {
    }

    public Sala(String titulo, String descricao) {
        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O nome da sala é obrigatório para sua criação.");
        }

        this.titulo = titulo;
        this.descricao = descricao;
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

}
