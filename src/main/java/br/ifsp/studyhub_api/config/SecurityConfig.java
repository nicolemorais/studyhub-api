package br.ifsp.studyhub_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${studyhub.security.professor.user}")
    private String profUser;

    @Value("${studyhub.security.professor.password}")
    private String profPassword;

    @Value("${studyhub.security.estudante.user}")
    private String studentUser;

    @Value("${studyhub.security.estudante.password}")
    private String studentPassword;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/salas").hasRole("PROFESSOR")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/salas/*").hasRole("PROFESSOR")
                        .anyRequest().authenticated())
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails professor = User.builder()
                .username(profUser)
                .password(passwordEncoder().encode(profPassword))
                .roles("PROFESSOR")
                .build();

        UserDetails estudante = User.builder()
                .username(studentUser)
                .password(passwordEncoder().encode(studentPassword))
                .roles("ESTUDANTE")
                .build();

        return new InMemoryUserDetailsManager(professor, estudante);
    }
}
