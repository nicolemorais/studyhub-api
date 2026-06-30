package br.com.studyhub.communication.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilita um broker em memória para enviar mensagens de volta aos clientes no
        // prefixo /topic
        config.enableSimpleBroker("/topic");

        // Prefixos das rotas de entrada na aplicação (mapeadas no @MessageMapping do
        // Controller)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint que o cliente usará para estabelecer a conexão WebSocket inicial
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*") // Configure adequadamente em produção
                .withSockJS(); // Fallback para navegadores antigos
    }
}