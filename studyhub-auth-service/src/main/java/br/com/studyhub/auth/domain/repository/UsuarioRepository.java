package br.com.studyhub.auth.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.studyhub.auth.domain.model.Usuario;
import br.com.studyhub.auth.interfaces.dto.CadastroRequestDTO;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);

    void registrarUsuario(CadastroRequestDTO request);
}
