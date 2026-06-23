package br.com.studyhub.auth.infrastruture.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.studyhub.auth.domain.model.PerfilUsuario;
import br.com.studyhub.auth.domain.model.Usuario;
import br.com.studyhub.auth.infrastructure.security.TokenService;

@DisplayName("Testes de Infraestrutura")
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private AutoCloseable closeable;

    @SuppressWarnings("null")
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        
        ReflectionTestUtils.setField(tokenService, "secret", "studyhub-test-secret-key-2026");
    }

    @AfterEach
    void tearDown() throws Exception {
        MDC.clear();
        closeable.close();
    }

    @Test
    @DisplayName("Deve gerar um token JWT válido contendo os claims corretos e o Correlation ID existente")
    void deveGerarTokenComClaimsECorrelationId() {

        Usuario usuario = new Usuario("professor@studyhub.com", "senhaHash", PerfilUsuario.PROFESSOR);
        ReflectionTestUtils.setField(usuario, "id", UUID.randomUUID());
        String correlationIdEsperado = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationIdEsperado);

        String token = tokenService.gerarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isBlank());

        DecodedJWT decodedJWT = JWT.decode(token);
        assertEquals("studyhub-auth-service", decodedJWT.getIssuer());
        assertEquals("professor@studyhub.com", decodedJWT.getSubject());
        assertEquals(PerfilUsuario.PROFESSOR.name(), decodedJWT.getClaim("perfil").asString());
        assertEquals(usuario.getId().toString(), decodedJWT.getClaim("id").asString());
        assertEquals(correlationIdEsperado, decodedJWT.getClaim("correlation_id").asString());
    }

    @Test
    @DisplayName("Deve gerar pioneiramente um Correlation ID se nenhum for encontrado no contexto MDC")
    void deveGerarCorrelationIdPioneiroNoToken() {

        Usuario usuario = new Usuario("aluno@studyhub.com", "senhaHash", PerfilUsuario.ALUNO);
        ReflectionTestUtils.setField(usuario, "id", UUID.randomUUID());

        String token = tokenService.gerarToken(usuario);

        DecodedJWT decodedJWT = JWT.decode(token);
        String correlationIdNoToken = decodedJWT.getClaim("correlation_id").asString();

        assertNotNull(correlationIdNoToken);
        assertDoesNotThrow(() -> UUID.fromString(correlationIdNoToken), "Deve ser um UUID válido.");
    }

    @Test
    @DisplayName("Deve validar um token legítimo e retornar o e-mail correspondente")
    void deveValidarTokenLegitimo() {

        Usuario usuario = new Usuario("aluno@studyhub.com", "senhaHash", PerfilUsuario.ALUNO);
        ReflectionTestUtils.setField(usuario, "id", UUID.randomUUID());
        String tokenGerado = tokenService.gerarToken(usuario);

        String subject = tokenService.validarToken(tokenGerado);

        assertEquals("aluno@studyhub.com", subject);
    }

    @Test
    @DisplayName("Deve retornar string vazia ao tentar validar um token corrompido, falso ou expirado")
    void deveRetornarVazioParaTokenInvalido() {
        // Act
        String subject = tokenService.validarToken("token-completamente-invalido-e-falso.jwt");

        // Assert
        assertTrue(subject.isEmpty(), "Tokens inválidos devem falhar silenciosamente retornando string vazia.");
    }
}