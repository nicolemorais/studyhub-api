package br.ifsp.studyhub_api.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${api.security.token.secret:${JWT_SECRET}}")
    private String secretKey;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = recuperarToken(request);

        if (token != null) {
            try {
                // Configura o algoritmo com a chave secreta compartilhada
                Algorithm algorithm = Algorithm.HMAC256(secretKey);

                DecodedJWT jwt = JWT.require(algorithm)
                        // .withIssuer("studyhub-auth-service") // Descomente se definiu um emissor no
                        // Auth Service
                        .build()
                        .verify(token);

                String subjectId = jwt.getSubject(); // O ID do usuário logado (UUID)

                String role = jwt.getClaim("role").asString();
                if (role == null)
                    role = "ROLE_USER";

                var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

                // Cria o contexto de autenticação do Spring Security com o ID Lógico do usuário
                var authentication = new UsernamePasswordAuthenticationToken(subjectId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JWTVerificationException ex) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token JWT invalido ou expirado.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}