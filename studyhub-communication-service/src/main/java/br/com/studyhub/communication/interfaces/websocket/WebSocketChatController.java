package br.com.studyhub.communication.interfaces.websocket;

import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import br.com.studyhub.communication.application.ChatService;
import br.com.studyhub.communication.domain.model.Mensagem;
import br.com.studyhub.communication.interfaces.dto.MensagemRequestDTO;

@Controller
public class WebSocketChatController {

    private final ChatService chatService;

    public WebSocketChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{salaId}/enviar")
    @SendTo("/topic/salas/{salaId}") // Broadcast para todos inscritos neste tópico
    public Mensagem enviarMensagemEmTempoReal(@DestinationVariable UUID salaId, @Payload MensagemRequestDTO request) {

        return chatService.processarNovaMensagem(
                salaId,
                request.remetenteId(),
                request.conteudoTexto(),
                request.nomeArquivo(),
                request.urlAnexo());
    }
}