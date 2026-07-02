package br.ifsp.studyhub_api.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.studyhub_api.dto.SalaRequestDTO;
import br.ifsp.studyhub_api.model.Sala;
import br.ifsp.studyhub_api.service.SalaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/salas")
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @PostMapping
    public ResponseEntity<Sala> criar(@RequestBody @Valid SalaRequestDTO request) {

        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID professorId = UUID.fromString(principal);

        Sala salaCriada = salaService.criarSala(request.titulo(), request.descricao(), professorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(salaCriada);
    }

    @PutMapping("/{salaId}/alunos")
    public ResponseEntity<Void> adicionarAlunoPorEmail(@PathVariable @NonNull UUID salaId, @RequestParam String email) {
        salaService.vincularAlunoPorEmail(salaId, email);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{salaId}/alunos/{alunoId}")
    public ResponseEntity<Void> removerAluno(@PathVariable @NonNull UUID salaId, @PathVariable UUID alunoId) {
        salaService.desvincularAluno(salaId, alunoId);
        return ResponseEntity.noContent().build();
    }
}