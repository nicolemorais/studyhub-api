package br.com.studyhub.communication.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import br.com.studyhub.communication.domain.event.MensagemEnviadaEvent;

@Component
public class MessageEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "studyhub.chat.messages";

    public MessageEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @SuppressWarnings("null")
    public void publicarMensagemEnviada(MensagemEnviadaEvent evento) {
        kafkaTemplate.send(TOPIC, evento.salaId().toString(), evento);
    }

}
