package br.ifsp.studyhub_api.security;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.ifsp.studyhub_api.model.Usuario;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {
    @Value("${api.security.token.secret:minha-chave}")
    private String secret;

    /**
     * Transforma o usuário autenticado em uma String criptografada.
     */
    public String gerarToken(Usuario usuario) {
        try {
            SecretKey chave = obterChaveAssinatura();

            return Jwts.builder()
                    .issuer("StudyHub-API")
                    .subject(usuario.getEmail())
                    .claim("nome", usuario.getNome())
                    .claim("perfil", usuario.getPerfil().name())
                    .issuedAt(new Date())
                    .expiration(gerarDataExpiracao())
                    .signWith(chave)
                    .compact();
        } catch (JwtException e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    /**
     * Lê o token enviado, verifica se a assinatura é legítima, se não expirou, 
     * e nos devolve o e-mail do usuário dono dele.
     */
    public String validarToken(String tokenJwt) {
        try {
            SecretKey chave = obterChaveAssinatura();

            return Jwts.parser()
                    .verifyWith(chave) 
                    .build()
                    .parseSignedClaims(tokenJwt) 
                    .getPayload()
                    .getSubject(); 
        } catch (JwtException e) {
            return null;
        }
    }

    private SecretKey obterChaveAssinatura() {
        return Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
    }

    private Date gerarDataExpiracao() {
        return Date.from(LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00")));
    }

}
