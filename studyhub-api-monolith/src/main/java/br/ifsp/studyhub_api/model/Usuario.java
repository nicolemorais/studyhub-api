package br.ifsp.studyhub_api.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.ifsp.studyhub_api.exception.BusinessException;
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
public class Usuario implements UserDetails {

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
    private Perfil perfil;

    private int pontos;
    
    protected Usuario() {}

    public Usuario(String nome, String email, String senhaCriptografada, Perfil perfil) {
        if (nome == null || nome.isBlank()) {
            throw new BusinessException("O nome do usuário é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new BusinessException("O e-mail é obrigatório.");
        }
        if (senhaCriptografada == null || senhaCriptografada.isBlank()) {
            throw new BusinessException("A senha é obrigatória.");
        }
        if (perfil == null) {
            throw new BusinessException("O perfil do usuário deve ser informado.");
        }

        this.nome = nome;
        this.email = email;
        this.senha = senhaCriptografada;
        this.perfil = perfil;
        this.pontos = 0;
    }

    
    /**
     * Adiciona pontuação ao perfil do aluno quando ele conclui tarefas.
     */
    public void acumularPontos(int quantidade) {
        if (this.perfil != Perfil.ALUNO) {
            throw new BusinessException("Apenas usuários do perfil Aluno podem acumular pontos.");
        }
        if (quantidade <= 0) {
            throw new BusinessException("A quantidade de pontos deve ser maior que zero.");
        }
        this.pontos += quantidade;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return List.of(new SimpleGrantedAuthority(this.perfil.getAuthority()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public Perfil getPerfil() { return perfil; }
    public int getPontos() { return pontos; }
}