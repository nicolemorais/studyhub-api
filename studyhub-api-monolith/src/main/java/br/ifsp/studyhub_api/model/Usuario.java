package br.ifsp.studyhub_api.model;

import java.util.UUID;

import br.ifsp.studyhub_api.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;


    protected Usuario() {
    }

    public Usuario(String nome, String email) {
        if (nome == null || nome.isBlank())
            throw new BusinessException("Nome obrigatório.");

        if (email == null || email.isBlank())
            throw new BusinessException("Email obrigatório.");

        this.nome = nome;
        this.email = email;
    }


    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}