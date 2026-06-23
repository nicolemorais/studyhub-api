package br.com.studyhub.auth.interfaces.dto;

import br.com.studyhub.auth.domain.model.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroRequestDTO(
        @NotBlank(message = "O e-mail é obrigatório.") @Email(message = "O e-mail fornecido é inválido.") String email,

        @NotBlank(message = "A senha é obrigatória.") @Size(min = 8, message = "A senha deve conter no mínimo 8 caracteres.") String senha,

        @NotNull(message = "O perfil do usuário é obrigatório.") PerfilUsuario perfil) {
}
