package br.ifsp.studyhub_api.controller;

import br.ifsp.studyhub_api.dto.GuiaRequestDTO;
import br.ifsp.studyhub_api.dto.GuiaResponseDTO;
import br.ifsp.studyhub_api.dto.SalaRequestDTO;
import br.ifsp.studyhub_api.dto.SalaResponseDTO;
import br.ifsp.studyhub_api.service.GuiaService;
import br.ifsp.studyhub_api.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;

import java.util.UUID;

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
