package br.com.studyhub.auth.interfaces.rest;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.studyhub.auth.application.service.UsuarioApplicationService;
import br.com.studyhub.auth.domain.model.Usuario;
import br.com.studyhub.auth.infrastructure.security.TokenService;
import br.com.studyhub.auth.interfaces.dto.CadastroRequestDTO;
import br.com.studyhub.auth.interfaces.dto.LoginRequestDTO;
import br.com.studyhub.auth.interfaces.dto.TokenResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação e Identidade", description = "Endpoints para gerenciamento de credenciais e tokens do ecossistema")
public class AuthenticationController {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioApplicationService usuarioService;

    public AuthenticationController(TokenService tokenService, PasswordEncoder passwordEncoder,
            UsuarioApplicationService usuarioService) {
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    @Operation(summary = "Efetuar Login", description = "Valida as credenciais fornecidas (e-mail e senha) e emite um token JWT válido contendo os claims de identidade e perfil do ecossistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso. Token JWT retornado no payload."),
            @ApiResponse(responseCode = "400", description = "Payload inválido. Dados obrigatórios ausentes ou mal formatados."),
            @ApiResponse(responseCode = "401", description = "Autenticação falhou. Credenciais inválidas ou usuário não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso bloqueado pela regra de negócio. A conta do usuário está inativa.")
    })
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        Usuario usuario = this.usuarioService.buscarPorEmail(data.email());

        if (!usuario.isAtivo()) {
            return ResponseEntity.status(403).build();
        }

        if (!passwordEncoder.matches(data.senha(), usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas.");
        }

        String token = tokenService.gerarToken(usuario);
        return ResponseEntity.ok(new TokenResponseDTO(token, "Bearer"));
    }

    @PostMapping("/registrar")
    @Operation(summary = "Registar Novo Usuário", description = "Regista uma nova credencial de acesso (Professor ou Aluno) de forma isolada no banco de identidades.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registado com sucesso. O cabeçalho 'Location' da resposta conterá a URI para o novo recurso."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida. Pode ocorrer devido a dados mal formatados, campos obrigatórios em falta ou violação da regra de negócio (ex: o e-mail fornecido já se encontra registado no ecossistema).")
    })
    public ResponseEntity<Void> registrar(@RequestBody @Valid CadastroRequestDTO data,
            UriComponentsBuilder uriBuilder) {

        Usuario novoUsuario = usuarioService.registrarUsuario(data);

        URI uri = uriBuilder.path("/api/v1/auth/usuarios/{id}").buildAndExpand(novoUsuario.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }
}
