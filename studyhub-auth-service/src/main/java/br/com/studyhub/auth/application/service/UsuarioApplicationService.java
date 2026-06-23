package br.com.studyhub.auth.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.studyhub.auth.domain.model.Usuario;
import br.com.studyhub.auth.domain.repository.UsuarioRepository;
import br.com.studyhub.auth.interfaces.dto.CadastroRequestDTO;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioApplicationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioApplicationService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrarUsuario(CadastroRequestDTO data) {
        if (this.usuarioRepository.findByEmail(data.email()).isPresent()) {
            throw new IllegalArgumentException("Não foi possível realizar o cadastro com este e-mail.");
        }

        String senhaCriptografada = passwordEncoder.encode(data.senha());
        Usuario novoUsuario = new Usuario(data.email(), senhaCriptografada, data.perfil());

        return this.usuarioRepository.save(novoUsuario);
    }

    public Usuario buscarPorEmail(String email) {
        return this.usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    }

}
