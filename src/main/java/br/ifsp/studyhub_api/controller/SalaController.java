package br.ifsp.studyhub_api.controller;

import br.ifsp.studyhub_api.dto.SalaRequestDTO;
import br.ifsp.studyhub_api.dto.SalaResponseDTO;
import br.ifsp.studyhub_api.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/salas")
public class SalaController {

    private final SalaService service;

    public SalaController(SalaService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<SalaResponseDTO> insert(@Valid @RequestBody SalaRequestDTO dto) {
        SalaResponseDTO response = service.insert(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
