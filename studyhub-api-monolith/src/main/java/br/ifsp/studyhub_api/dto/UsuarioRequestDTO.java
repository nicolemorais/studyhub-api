package br.ifsp.studyhub_api.dto;

import br.ifsp.studyhub_api.model.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UsuarioRequestDTO(

    @NotBlank(message = "O nome é obrigatório.")
    String nome,

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail informado deve ser válido.")
    String email,

    @NotBlank(message = "A senha é obrigatória.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$", message = "A senha deve ser segura: mínimo de 8 caracteres, contendo pelo menos 1 letra maiúscula, 1 minúscula, 1 número e 1 caractere especial (@#$%^&+=!).")
    String senha,

    @NotNull(message = "O perfil do usuário (PROFESSOR ou ALUNO) é obrigatório.")
    Perfil perfil

){}
