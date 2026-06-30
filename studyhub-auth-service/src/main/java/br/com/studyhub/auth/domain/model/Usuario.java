package br.com.studyhub.auth.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfil;

    @Column(nullable = false)
    private boolean ativo;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    protected Usuario() {
    }

    public Usuario(String nome, String email, String senhaCriptografada, PerfilUsuario perfil) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("O nome é obrigatório.");

        if (email == null || email.isBlank())
            throw new IllegalArgumentException("O e-mail é obrigatório.");

        if (senhaCriptografada == null || senhaCriptografada.isBlank())
            throw new IllegalArgumentException("A senha é obrigatória.");

        if (perfil == null)
            throw new IllegalArgumentException("O perfil do usuário é obrigatório.");

        this.nome = nome;
        this.email = email;
        this.senha = senhaCriptografada;
        this.perfil = perfil;
        this.ativo = true;
        this.dataCriacao = LocalDateTime.now();
    }

    public void desativarAcesso() {
        this.ativo = false;
    }

    public void reativarAcesso() {
        this.ativo = true;
    }

    public void alterarSenha(String novaSenhaCriptografada) {
        if (novaSenhaCriptografada == null || novaSenhaCriptografada.isBlank()) {
            throw new IllegalArgumentException("A nova senha não pode ser vazia.");
        }
        this.senha = novaSenhaCriptografada;
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

    public String getSenha() {
        return senha;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public boolean isAtivo() {
        return ativo;
    }

}
