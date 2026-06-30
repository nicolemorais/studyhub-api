package br.ifsp.studyhub_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.studyhub_api.dto.GuiaRequestDTO;
import br.ifsp.studyhub_api.dto.GuiaResponseDTO;
import br.ifsp.studyhub_api.dto.MatriculaDTO;
import br.ifsp.studyhub_api.dto.SalaAlunosResponseDTO;
import br.ifsp.studyhub_api.dto.SalaRequestDTO;
import br.ifsp.studyhub_api.dto.SalaResponseDTO;
import br.ifsp.studyhub_api.service.GuiaService;
import br.ifsp.studyhub_api.service.SalaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/salas")
public class SalaController {

    private final SalaService salaService;
    private final GuiaService guiaService;

    public SalaController(SalaService salaService, GuiaService guiaService) {
        this.salaService = salaService;
        this.guiaService = guiaService;
    }

    /**
     * Retorna as salas em lotes controlados.
     * Rota completa: GET /api/v1/salas?page=0&size=10
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('PROFESSOR', 'ALUNO')")
    public ResponseEntity<Page<SalaResponseDTO>> findAll(
            @PageableDefault(size = 10, sort = "titulo") Pageable pageable) {

        Page<SalaResponseDTO> page = salaService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<SalaResponseDTO> insert(@Valid @RequestBody SalaRequestDTO dto) {
        SalaResponseDTO response = salaService.insert(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> delete(@PathVariable @NonNull UUID id) {
        salaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/matricular")
    @PreAuthorize("hasAnyRole('PROFESSOR')")
    public ResponseEntity<Void> matricularAluno(
            @PathVariable @NonNull UUID id,
            @Valid @RequestBody MatriculaDTO dto) {

        salaService.matricularAluno(id, dto.email());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/professor/minhas-salas")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<SalaAlunosResponseDTO>> listarMinhasSalasComoProfessor(Authentication authentication) {
        String emailProfessor = authentication.getName();

        List<SalaAlunosResponseDTO> response = salaService.listarSalasEAlunosDoProfessor(emailProfessor);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/aluno/minhas-salas")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<List<SalaAlunosResponseDTO>> listarMinhasSalasComoAluno(Authentication authentication) {
        String emailAluno = authentication.getName();

        List<SalaAlunosResponseDTO> response = salaService.listarSalasEAlunosDoAluno(emailAluno);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/alunos/{alunoId}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> removerAluno(
            @PathVariable @NonNull UUID id,
            @PathVariable @NonNull UUID alunoId) {

        salaService.removerAluno(id, alunoId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para criar um roteiro pedagógico dentro de uma sala.
     * Rota completa: POST /api/v1/salas/{salaId}/guias
     */
    @PostMapping("/{salaId}/guias")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<GuiaResponseDTO> criarGuia(
            @PathVariable @NonNull UUID salaId,
            @Valid @RequestBody GuiaRequestDTO dto) {

        GuiaResponseDTO response = guiaService.criar(salaId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
