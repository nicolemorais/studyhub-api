package br.com.studyhub.communication.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.studyhub.communication.domain.event.MensagemEnviadaEvent;
import br.com.studyhub.communication.domain.model.Mensagem;
import br.com.studyhub.communication.domain.repository.MessagemRepository;
import br.com.studyhub.communication.infrastructure.messaging.MessageEventProducer;

@Service
public class ChatService {

    private final MessagemRepository repository;
    private final MessageEventProducer producer;

    public ChatService(MessagemRepository repository, MessageEventProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    @Transactional
    public Mensagem processarNovaMensagem(UUID salaId, UUID remetenteId, String texto, String nomeArquivo,
            String urlAnexo) {
        Mensagem mensagem = new Mensagem(salaId, remetenteId, texto, nomeArquivo, urlAnexo);
        Mensagem mensagemSalva = repository.save(mensagem);

        // Disparo assíncrono para notificar Gamificação, Auditoria, etc.
        MensagemEnviadaEvent evento = new MensagemEnviadaEvent(
                mensagemSalva.getId(),
                mensagemSalva.getSalaId(),
                mensagemSalva.getRemetenteId(),
                mensagemSalva.getEnviadaEm());
        producer.publicarMensagemEnviada(evento);

        return mensagemSalva;
    }

    @Transactional(readOnly = true)
    public Page<Mensagem> buscarHistorico(UUID salaId, String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isBlank()) {
            return repository.findBySalaIdAndConteudoTextoContainingIgnoreCase(salaId, keyword, pageable);
        }
        return repository.findBySalaId(salaId, pageable);
    }
}