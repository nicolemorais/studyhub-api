package br.ifsp.studyhub_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ifsp.studyhub_api.dto.MatriculaDTO;
import br.ifsp.studyhub_api.dto.SalaRequestDTO;
import br.ifsp.studyhub_api.dto.SalaResponseDTO;
import br.ifsp.studyhub_api.exception.ResourceNotFoundException;
import br.ifsp.studyhub_api.repository.UsuarioRepository;
import br.ifsp.studyhub_api.security.SecurityConfigurations;
import br.ifsp.studyhub_api.security.TokenService;
import br.ifsp.studyhub_api.service.SalaService;
import br.ifsp.studyhub_api.service.GuiaService;

@WebMvcTest(SalaController.class)
@Import(SecurityConfigurations.class)
@SuppressWarnings("null")
public class SalaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final UUID salaId = UUID.randomUUID();
    private final UUID alunoId = UUID.randomUUID();

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GuiaService guiaService;

    @MockitoBean
    private SalaService salaService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    // --- TESTES DE CRIAÇÃO (US 1.1) ---

    @Test
    @WithMockUser(roles = "PROFESSOR")
    @DisplayName("US 1.1 - CA1: Deve retornar 201 Created quando o Professor enviar dados válidos")
    public void insertShouldReturnCreatedAndPayloadWhenDataIsValid() throws Exception {
        SalaRequestDTO requestDTO = new SalaRequestDTO("Arquitetura de Computadores", "Turma de Engenharia de 2026");
        UUID expectedId = UUID.randomUUID();
        SalaResponseDTO responseDTO = new SalaResponseDTO(expectedId, requestDTO.titulo(), requestDTO.descricao());

        Mockito.when(salaService.insert(any(SalaRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/salas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedId.toString()))
                .andExpect(jsonPath("$.titulo").value("Arquitetura de Computadores"));
    }

    @Test
    @WithMockUser(roles = "ESTUDANTE")
    @DisplayName("US 1.1 - CA3: Deve retornar 403 Forbidden se um Estudante tentar criar uma sala")
    public void insertShouldReturnForbiddenWhenUserIsStudent() throws Exception {
        SalaRequestDTO requestDTO = new SalaRequestDTO("Sala Secreta", "Tentativa de burlar");

        mockMvc.perform(post("/salas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PROFESSOR")
    @DisplayName("US 1.1 - CA2: Deve retornar 400 Bad Request ao omitir o nome da sala")
    public void insertShouldReturnBadRequestWhenNameIsBlank() throws Exception {
        SalaRequestDTO invalidRequest = new SalaRequestDTO("", "Tentativa sem nome");

        mockMvc.perform(post("/salas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Erro de Validação de Sintaxe"));
    }

    // --- TESTES DE EXCLUSÃO (US 1.2) ---

    @Test
    @WithMockUser(roles = "PROFESSOR")
    @DisplayName("US 1.2 - CA1: Deve retornar 204 No Content ao deletar id existente como Professor")
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        UUID existingId = UUID.randomUUID();
        Mockito.doNothing().when(salaService).delete(existingId);

        mockMvc.perform(delete("/salas/{id}", existingId)
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ESTUDANTE")
    @DisplayName("US 1.2 - CA3: Deve retornar 403 Forbidden se um Estudante tentar deletar uma sala")
    public void deleteShouldReturnForbiddenWhenUserIsStudent() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/salas/{id}", id)
                .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PROFESSOR")
    @DisplayName("US 1.2 - CA2: Deve retornar 404 Not Found se o ID não existir")
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        UUID nonExistingId = UUID.randomUUID();
        Mockito.doThrow(new ResourceNotFoundException("Sala não encontrada")).when(salaService).delete(nonExistingId);

        mockMvc.perform(delete("/salas/{id}", nonExistingId)
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve permitir que PROFESSOR matricule um aluno e retornar 204 No Content")
    @WithMockUser(roles = "PROFESSOR")
    void matricularAlunoComoProfessor() throws Exception {
        String emailAluno = "aluno.teste@ifsp.edu.br";
        MatriculaDTO dto = new MatriculaDTO(emailAluno);

        doNothing().when(salaService).matricularAluno(any(UUID.class), eq(emailAluno));

        mockMvc.perform(post("/salas/{id}/matricular", salaId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve barrar ALUNO que tentar matricular alguém e retornar 403 Forbidden")
    @WithMockUser(roles = "ALUNO")
    void matricularAlunoComoAlunoDeveSerBarrado() throws Exception {
        String emailAluno = "aluno.teste@ifsp.edu.br";
        MatriculaDTO dto = new MatriculaDTO(emailAluno);

        mockMvc.perform(post("/salas/{id}/matricular", salaId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve permitir que PROFESSOR remova um aluno e retornar 204 No Content")
    @WithMockUser(roles = "PROFESSOR")
    void removerAlunoComoProfessor() throws Exception {
        doNothing().when(salaService).removerAluno(salaId, alunoId);

        mockMvc.perform(delete("/salas/{id}/alunos/{alunoId}", salaId, alunoId)
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve barrar ALUNO que tentar remover alguém e retornar 403 Forbidden")
    @WithMockUser(roles = "ALUNO")
    void removerAlunoComoAlunoDeveSerBarrado() throws Exception {
        mockMvc.perform(delete("/salas/{id}/alunos/{alunoId}", salaId, alunoId)
                .with(csrf()))
                .andExpect(status().isForbidden());
    }
}