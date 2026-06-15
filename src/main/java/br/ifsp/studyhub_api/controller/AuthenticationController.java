package br.ifsp.studyhub_api.controller;

import br.ifsp.studyhub_api.dto.LoginRequestDTO;
import br.ifsp.studyhub_api.dto.TokenResponseDTO;
import br.ifsp.studyhub_api.dto.UsuarioRequestDTO;
import br.ifsp.studyhub_api.exception.BusinessException;
import br.ifsp.studyhub_api.model.Usuario;
import br.ifsp.studyhub_api.repository.UsuarioRepository;
import br.ifsp.studyhub_api.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository,
            TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra um novo usuário .
     * Rota completa: POST /api/v1/auth/cadastro
     */
    @PostMapping("/cadastro")
    public ResponseEntity<Void> cadastrar(@Valid @RequestBody UsuarioRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new BusinessException("Não foi possível concluir o cadastro com os dados informados.");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.senha());

        Usuario novoUsuario = new Usuario(dto.nome(), dto.email(), senhaCriptografada, dto.perfil());

        usuarioRepository.save(novoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Autentica o usuário e devolve o Token JWT.
     * Rota completa: POST /api/v1/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        try {
            var dadosLogin = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());

            var authentication = authenticationManager.authenticate(dadosLogin);

            String tokenJwt = tokenService.gerarToken((Usuario) authentication.getPrincipal());

            return ResponseEntity.ok(new TokenResponseDTO(tokenJwt));
        } catch (Exception e) {
            throw new BusinessException("E-mail ou senha inválidos.");
        }
    }
}