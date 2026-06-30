package br.com.studyhub.communication.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.studyhub.communication.domain.event.MensagemEnviadaEvent;
import br.com.studyhub.communication.domain.model.Mensagem;
import br.com.studyhub.communication.domain.repository.MessagemRepository;
import br.com.studyhub.communication.infrastructure.messaging.MessageEventProducer;

public class ChatServiceTest {
    @Mock
    private MessagemRepository repository;

    @Mock
    private MessageEventProducer producer;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve rejeitar arquivo .exe com SecurityException")
    void deveRejeitarArquivoExe() {
        UUID salaId = UUID.randomUUID();
        UUID remetenteId = UUID.randomUUID();

        SecurityException exception = assertThrows(SecurityException.class, () -> {
            chatService.processarNovaMensagem(salaId, remetenteId, "Envio trabalho", "virus.exe",
                    "http://aws/virus.exe");
        });

        assertTrue(exception.getMessage().contains("perigosas"));
        verify(repository, never()).save(any(Mensagem.class));
        verify(producer, never()).publicarMensagemEnviada(any());
    }

    @Test
    @DisplayName("Deve rejeitar arquivo .bat com SecurityException")
    void deveRejeitarArquivoBat() {
        UUID salaId = UUID.randomUUID();
        UUID remetenteId = UUID.randomUUID();

        assertThrows(SecurityException.class, () -> {
            chatService.processarNovaMensagem(salaId, remetenteId, "Script malicioso", "script.bat",
                    "http://aws/script.bat");
        });
    }

    @Test
    @DisplayName("Deve rejeitar mensagem com texto vazio")
    void deveRejeitarTextoVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            chatService.processarNovaMensagem(UUID.randomUUID(), UUID.randomUUID(), "   ", null, null);
        });
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve processar mensagem válida, salvar e publicar no Kafka")
    void deveProcessarEPublicarNoKafka() {

        UUID salaId = UUID.randomUUID();
        UUID remetenteId = UUID.randomUUID();
        String texto = "Dúvida na questão 2";

        when(repository.save(any(Mensagem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mensagem resultado = chatService.processarNovaMensagem(salaId, remetenteId, texto, "foto_caderno.png",
                "http://aws/foto.png");
        assertNotNull(resultado);
        verify(repository, times(1)).save(any(Mensagem.class));

        ArgumentCaptor<MensagemEnviadaEvent> captor = ArgumentCaptor.forClass(MensagemEnviadaEvent.class);
        verify(producer, times(1)).publicarMensagemEnviada(captor.capture());

        MensagemEnviadaEvent eventoDisparado = captor.getValue();
        assertEquals(salaId, eventoDisparado.salaId());
        assertEquals(remetenteId, eventoDisparado.remetenteId());
    }
}
