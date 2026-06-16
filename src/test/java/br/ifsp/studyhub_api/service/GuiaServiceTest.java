package br.ifsp.studyhub_api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ifsp.studyhub_api.dto.GuiaPutRequestDTO;
import br.ifsp.studyhub_api.dto.GuiaRequestDTO;
import br.ifsp.studyhub_api.dto.GuiaResponseDTO;
import br.ifsp.studyhub_api.dto.TopicoPutRequestDTO;
import br.ifsp.studyhub_api.dto.TopicoRequestDTO;
import br.ifsp.studyhub_api.exception.BusinessException;
import br.ifsp.studyhub_api.exception.ResourceNotFoundException;
import br.ifsp.studyhub_api.model.Guia;
import br.ifsp.studyhub_api.model.Sala;
import br.ifsp.studyhub_api.model.Topico;
import br.ifsp.studyhub_api.model.Usuario;
import br.ifsp.studyhub_api.repository.GuiaRepository;
import br.ifsp.studyhub_api.repository.SalaRepository;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class GuiaServiceTest {

    @Mock
    private GuiaRepository guiaRepository;

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private GuiaService guiaService;

    private Sala salaMock;
    private UUID salaId;
    private UUID guiaId;
    private Usuario professorMock;

    @BeforeEach
    void setUp() {
        salaId = UUID.randomUUID();
        guiaId = UUID.randomUUID();
        professorMock = Mockito.mock(Usuario.class);

        salaMock = new Sala("Teste", "Testando o GuiaService", professorMock);
    }

    @Test
    @DisplayName("Deve criar um guia de estudos com sucesso quando os dados forem válidos")
    void criarComSucesso() {
        TopicoRequestDTO topicoDto = new TopicoRequestDTO("Introdução", "Conteúdo inicial da ementa");
        GuiaRequestDTO requestDto = new GuiaRequestDTO("Módulo 1", List.of(topicoDto),
                List.of("http://teste.com/aula.pdf"));

        when(salaRepository.findById(salaId)).thenReturn(Optional.of(salaMock));
        when(guiaRepository.save(any(Guia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GuiaResponseDTO response = guiaService.criar(salaId, requestDto);

        assertNotNull(response);
        assertEquals("Módulo 1", response.titulo());
        assertEquals(1, response.topicos().size());
        assertEquals("Introdução", response.topicos().get(0).titulo());
        verify(guiaRepository, times(1)).save(any(Guia.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao tentar criar guia com arquivos executáveis")
    void criarComArquivoProibido() {
        TopicoRequestDTO topicoDto = new TopicoRequestDTO("Álgebra", "Descrição");
        GuiaRequestDTO requestDto = new GuiaRequestDTO("Guia Bloqueado", List.of(topicoDto),
                List.of("arquivo.exe"));

        when(salaRepository.findById(salaId)).thenReturn(Optional.of(salaMock));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            guiaService.criar(salaId, requestDto);
        });

        assertEquals("Não é permitido o upload de arquivos executáveis.",
                exception.getMessage());
        verify(guiaRepository, never()).save(any(Guia.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao tentar atualizar um guia removendo todos os tópicos")
    void atualizarRemovendoTodosOsTopicos() {
        GuiaPutRequestDTO requestPutDto = new GuiaPutRequestDTO("Título Atualizado", List.of(), new ArrayList<>());

        List<Guia.DadosTopico> topicosIniciais = List
                .of(new Guia.DadosTopico(UUID.randomUUID(), "Tópico 1", "Descrição"));
        Guia guiaExistente = new Guia("Guia Antigo", salaMock, topicosIniciais);

        when(guiaRepository.findById(guiaId)).thenReturn(Optional.of(guiaExistente));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            guiaService.atualizar(guiaId, requestPutDto);
        });

        assertEquals("A guia deve conter pelo menos 1 tópico ativo.", exception.getMessage());
        verify(guiaRepository, never()).save(any(Guia.class));
    }

    @Test
    @DisplayName("Deve atualizar a ementa mantendo apenas um tópico ativo quando multiplos tópicos existirem")
    void atualizarRemovendoTopicosParcialmente() {
        UUID topicoManterId = UUID.randomUUID();
        TopicoPutRequestDTO topicoManterDto = new TopicoPutRequestDTO(topicoManterId, "Tópico que Fica",
                "Nova Descrição");
        GuiaPutRequestDTO requestPutDto = new GuiaPutRequestDTO("Guia Atualizado", List.of(topicoManterDto),
                new ArrayList<>());

        List<Guia.DadosTopico> dadosIniciais = List.of(
                new Guia.DadosTopico(null, "Tópico 1", "Descrição Antiga"),
                new Guia.DadosTopico(null, "Tópico Deletado", "Será removido"));
        Guia guiaExistente = new Guia("Guia Antigo", salaMock, dadosIniciais);

        org.springframework.test.util.ReflectionTestUtils.setField(guiaExistente, "id", guiaId);

        List<Topico> topicosInternos = guiaExistente.getTopicos();
        org.springframework.test.util.ReflectionTestUtils.setField(topicosInternos.get(0), "id", topicoManterId);
        org.springframework.test.util.ReflectionTestUtils.setField(topicosInternos.get(1), "id", UUID.randomUUID());

        when(guiaRepository.findById(guiaId)).thenReturn(Optional.of(guiaExistente));
        when(guiaRepository.save(any(Guia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GuiaResponseDTO response = guiaService.atualizar(guiaId, requestPutDto);

        assertNotNull(response);
        assertEquals(1, response.topicos().size());
        assertEquals("Tópico que Fica", response.topicos().get(0).titulo());
        assertEquals("Nova Descrição", response.topicos().get(0).descricao());
        verify(guiaRepository, times(1)).save(any(Guia.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar atualizar um guia com ID inexistente")
    void atualizarIdInexistente() {

        GuiaPutRequestDTO requestPutDto = new GuiaPutRequestDTO("Título", List.of(), new ArrayList<>());
        when(guiaRepository.findById(guiaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            guiaService.atualizar(guiaId, requestPutDto);
        });

        verify(guiaRepository, never()).save(any(Guia.class));
    }

    @Test
    @DisplayName("Deve excluir um guia de estudos e suas dependências com sucesso")
    void excluirComSucesso() {
        when(guiaRepository.existsById(guiaId)).thenReturn(true);

        assertDoesNotThrow(() -> guiaService.excluir(guiaId));

        verify(guiaRepository, times(1)).existsById(guiaId);
        verify(guiaRepository, times(1)).deleteById(guiaId);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar excluir um guia com ID inexistente")
    void excluirIdInexistente() {
        
      when(guiaRepository.existsById(guiaId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            guiaService.excluir(guiaId);
        });
        
        verify(guiaRepository, times(1)).existsById(guiaId);
        verify(guiaRepository, never()).deleteById(any());
    }
}
