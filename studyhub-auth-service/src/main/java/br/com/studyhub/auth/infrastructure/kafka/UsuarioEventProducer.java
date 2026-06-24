package br.com.studyhub.auth.infrastructure.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import br.com.studyhub.auth.domain.event.UsuarioRegistradoEvent;

@Component
public class UsuarioEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "auth.usuario.registrado";

    public UsuarioEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @SuppressWarnings("null")
    public void publicarUsuarioRegistrado(UsuarioRegistradoEvent event) {
        kafkaTemplate.send(TOPIC, event.id().toString(), event);
    }
}
