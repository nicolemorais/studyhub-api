package br.ifsp.studyhub_api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ifsp.studyhub_api.dto.SalaAlunosResponseDTO;
import br.ifsp.studyhub_api.model.Perfil;
import br.ifsp.studyhub_api.model.Sala;
import br.ifsp.studyhub_api.model.Usuario;
import br.ifsp.studyhub_api.repository.SalaRepository;
import br.ifsp.studyhub_api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SalaService salaService;

    private UUID salaId;
    private UUID alunoId;
    private String emailAluno;
    private Sala salaMock;
    private Usuario alunoMock;
    private Usuario professorMock;

    @BeforeEach
    void setUp() {
        salaId = UUID.randomUUID();
        alunoId = UUID.randomUUID();
        emailAluno = "aluno.teste@ifsp.edu.br";

        professorMock = mock(Usuario.class);
        alunoMock = mock(Usuario.class);

        salaMock = new Sala("Java Avançado", "Sala de testes", professorMock);
    }

    @Test
    @DisplayName("Deve matricular um aluno com sucesso na sala")
    void matricularAlunoComSucesso() {
        when(alunoMock.getPerfil()).thenReturn(Perfil.ALUNO);

        when(salaRepository.findById(salaId)).thenReturn(Optional.of(salaMock));
        when(usuarioRepository.findByEmail(emailAluno)).thenReturn(Optional.of(alunoMock));

        assertDoesNotThrow(() -> salaService.matricularAluno(salaId, emailAluno));

        assertTrue(salaMock.getAlunos().contains(alunoMock), "O aluno deveria estar na lista de matriculados");
        verify(salaRepository, times(1)).save(salaMock);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a sala não for encontrada na matrícula")
    void matricularAlunoSalaNaoEncontrada() {
        when(salaRepository.findById(salaId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salaService.matricularAluno(salaId, emailAluno);
        });

        assertEquals("Sala não encontrada.", exception.getMessage());
        verify(usuarioRepository, never()).findByEmail(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o aluno não for encontrado na matrícula")
    void matricularAlunoNaoExiste() {
        when(salaRepository.findById(salaId)).thenReturn(Optional.of(salaMock));
        when(usuarioRepository.findByEmail(emailAluno)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salaService.matricularAluno(salaId, emailAluno);
        });

        assertEquals("Aluno não encontrado.", exception.getMessage());
        verify(salaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve remover um aluno da sala com sucesso")
    void removerAlunoComSucesso() {
        salaMock.adicionarAluno(alunoMock);

        when(salaRepository.findById(salaId)).thenReturn(Optional.of(salaMock));
        when(usuarioRepository.findById(alunoId)).thenReturn(Optional.of(alunoMock));

        assertDoesNotThrow(() -> salaService.removerAluno(salaId, alunoId));

        assertFalse(salaMock.getAlunos().contains(alunoMock), "O aluno não deveria mais estar na lista");
        verify(salaRepository, times(1)).save(salaMock);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a sala não for encontrada na remoção")
    void removerAlunoSalaNaoEncontrada() {
        when(salaRepository.findById(salaId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            salaService.removerAluno(salaId, alunoId);
        });

        verify(salaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar salas e alunos de um professor com sucesso")
    void listarSalasEAlunosDoProfessorComSucesso() {
        String emailProf = "professor@ifsp.edu.br";
        when(salaRepository.buscarSalasEProfessorComAlunos(emailProf)).thenReturn(List.of(salaMock));

        List<SalaAlunosResponseDTO> resultado = salaService.listarSalasEAlunosDoProfessor(emailProf);

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(salaMock.getTitulo(), resultado.get(0).titulo());
        verify(salaRepository, times(1)).buscarSalasEProfessorComAlunos(emailProf);
    }

    @Test
    @DisplayName("Deve listar salas e companheiros de classe de um aluno com sucesso")
    void listarSalasEAlunosDoAlunoComSucesso() {
        String emailAluno = "aluno@ifsp.edu.br";
        when(salaRepository.buscarSalasEAlunosPorAlunoEmail(emailAluno)).thenReturn(List.of(salaMock));

        List<SalaAlunosResponseDTO> resultado = salaService.listarSalasEAlunosDoAluno(emailAluno);

        assertFalse(resultado.isEmpty());
        assertEquals(salaMock.getTitulo(), resultado.get(0).titulo());
        verify(salaRepository, times(1)).buscarSalasEAlunosPorAlunoEmail(emailAluno);
    }

}