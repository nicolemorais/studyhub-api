package br.com.studyhub.auth.domain.event;

import java.util.UUID;

import org.springframework.lang.NonNull;

public record UsuarioRegistradoEvent(@NonNull UUID id, String email, String perfil) {

}
