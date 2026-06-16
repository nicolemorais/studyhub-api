package br.ifsp.studyhub_api.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.studyhub_api.dto.GuiaPutRequestDTO;
import br.ifsp.studyhub_api.dto.GuiaResponseDTO;
import br.ifsp.studyhub_api.service.GuiaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/guias")
public class GuiaController {

    private final GuiaService service;

    public GuiaController(GuiaService service) {
        this.service = service;
    }

    /**
     * Endpoint para atualizar completamente ou parcialmente o guia, tópicos e mídias[cite: 54, 56].
     * Rota completa: PUT /api/v1/guias/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<GuiaResponseDTO> atualizar(
            @PathVariable @NonNull UUID id,
            @Valid @RequestBody GuiaPutRequestDTO dto) {
        GuiaResponseDTO response = service.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para deletar o guia e limpar o banco.
     * Rota completa: DELETE /api/v1/guias/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> excluir(@PathVariable @NonNull UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
