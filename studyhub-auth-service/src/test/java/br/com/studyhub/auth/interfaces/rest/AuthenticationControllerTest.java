package br.com.studyhub.auth.interfaces.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.studyhub.auth.application.service.UsuarioApplicationService;
import br.com.studyhub.auth.domain.model.PerfilUsuario;
import br.com.studyhub.auth.domain.model.Usuario;
import br.com.studyhub.auth.domain.repository.UsuarioRepository;
import br.com.studyhub.auth.infrastructure.security.TokenService;
import br.com.studyhub.auth.interfaces.dto.CadastroRequestDTO;
import br.com.studyhub.auth.interfaces.dto.LoginRequestDTO;
import br.com.studyhub.auth.interfaces.dto.TokenResponseDTO;

@DisplayName("Testes Adaptadores Rest")
public class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController controller;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioApplicationService usuarioService;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------------------------------------------------
    // TESTES DE LOGIN (Buscam no Repository)
    // ----------------------------------------------------

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve autenticar o usuário e retornar o token Bearer quando as credenciais estiverem corretas")
    void deveAutenticarComSucesso() {

        LoginRequestDTO request = new LoginRequestDTO("aluno@studyhub.com", "senha123");
        Usuario usuarioMock = new Usuario("Luciano Silva","aluno@studyhub.com", "hashSenhaCriptografada", PerfilUsuario.ALUNO);

        when(usuarioService.buscarPorEmail(request.email())).thenReturn(usuarioMock);
        when(passwordEncoder.matches(request.senha(), usuarioMock.getSenha())).thenReturn(true);
        when(tokenService.gerarToken(usuarioMock)).thenReturn("jwt-token-valido-gerado");

        ResponseEntity<TokenResponseDTO> response = controller.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt-token-valido-gerado", response.getBody().token());
        assertEquals("Bearer", response.getBody().tipo());

        verify(usuarioService, times(1)).buscarPorEmail(request.email());
        verify(passwordEncoder, times(1)).matches(request.senha(), usuarioMock.getSenha());
        verify(tokenService, times(1)).gerarToken(usuarioMock);
    }

    @Test
    @DisplayName("Deve lançar exceção genérica de falha se a senha enviada não bater com o hash cadastrado")
    void deveLancarExcecaoParaSenhaIncorreta() {

        LoginRequestDTO request = new LoginRequestDTO("aluno@studyhub.com", "senhaIncorreta");
        Usuario usuarioMock = new Usuario("Luciano Silva","aluno@studyhub.com", "hashSenhaCorreta", PerfilUsuario.ALUNO);

        when(usuarioService.buscarPorEmail(request.email())).thenReturn(usuarioMock);
        when(passwordEncoder.matches(request.senha(), usuarioMock.getSenha())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            controller.login(request);
        });

        assertEquals("Credenciais inválidas.", exception.getMessage());
        verify(tokenService, never()).gerarToken(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve barrar e retornar status 403 Forbidden se a regra de negócio detectar que a conta está desativada")
    void deveRetornarForbiddenParaUsuarioInativo() {

        LoginRequestDTO request = new LoginRequestDTO("professor@studyhub.com", "senha123");
        Usuario usuarioMock = new Usuario("Luciano Silva","professor@studyhub.com", "hashSenha", PerfilUsuario.PROFESSOR);
        usuarioMock.desativarAcesso(); // Cenário de conta inativada

        when(usuarioService.buscarPorEmail(request.email())).thenReturn(usuarioMock);

        ResponseEntity<TokenResponseDTO> response = controller.login(request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody(), "O payload de resposta deve ser nulo para acessos proibidos.");

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(tokenService, never()).gerarToken(any(Usuario.class));
    }

    // ----------------------------------------------------
    // TESTES DE REGISTRO (Delegam para o Application Service)
    // ----------------------------------------------------

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso repassando para o Service")
    void deveRegistrarUsuarioComSucesso() {

        CadastroRequestDTO request = new CadastroRequestDTO("Luciano Silva","novo@studyhub.com", "senha123", PerfilUsuario.ALUNO);
        org.springframework.web.util.UriComponentsBuilder uriBuilder = org.springframework.web.util.UriComponentsBuilder
                .newInstance();

        Usuario usuarioMock = new Usuario("Luciano Silva","novo@studyhub.com", "hash", PerfilUsuario.ALUNO);
        ReflectionTestUtils.setField(usuarioMock, "id", UUID.randomUUID());

        when(usuarioService.registrarUsuario(request)).thenReturn(usuarioMock);

        ResponseEntity<Void> response = controller.registrar(request, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(usuarioService, times(1)).registrarUsuario(request);
    }

    @Test
    @DisplayName("Deve permitir que a exceção do Service suba quando o e-mail for duplicado")
    void deveLancarExcecaoParaEmailDuplicado() {
        CadastroRequestDTO request = new CadastroRequestDTO("Luciano Silva","duplicado@studyhub.com", "senha123",
                PerfilUsuario.PROFESSOR);
        org.springframework.web.util.UriComponentsBuilder uriBuilder = org.springframework.web.util.UriComponentsBuilder
                .newInstance();

        when(usuarioService.registrarUsuario(any(CadastroRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Não foi possível realizar o cadastro com este e-mail."));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.registrar(request, uriBuilder);
        });

        assertEquals("Não foi possível realizar o cadastro com este e-mail.", exception.getMessage());
    }
}