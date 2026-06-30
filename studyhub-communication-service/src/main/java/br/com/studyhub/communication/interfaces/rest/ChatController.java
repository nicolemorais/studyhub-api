package br.com.studyhub.communication.interfaces.rest;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.studyhub.communication.application.ChatService;
import br.com.studyhub.communication.domain.model.Mensagem;
import br.com.studyhub.communication.interfaces.dto.PaginaRequestDTO;

@RestController
@RequestMapping("/api/v1/chat/salas/{salaId}/mensagens")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<PaginaRequestDTO<Mensagem>> listarHistorico(
            @PathVariable UUID salaId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "enviadaEm"));
        Page<Mensagem> pagina = chatService.buscarHistorico(salaId, keyword, pageRequest);

        PaginaRequestDTO<Mensagem> resposta = new PaginaRequestDTO<>(
                pagina.getContent(),
                pagina.getNumber(),
                pagina.getTotalPages(),
                pagina.getTotalElements());

        return ResponseEntity.ok(resposta);
    }
}