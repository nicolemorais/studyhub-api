package br.ifsp.studyhub_api.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.studyhub_api.dto.TopicoRequestDTO;
import br.ifsp.studyhub_api.dto.TopicoResponseDTO;
import br.ifsp.studyhub_api.service.TopicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;

@RestController
@RequestMapping("/guias/{guiaId}/topicos")
@Validated
@Tag(name = "Tópicos", description = "Operações relacionadas aos tópicos de um guia")
public class TopicoController {

    private final TopicoService topicoService;

    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    @Operation(summary = "Criar tópico", description = "Cria um novo tópico para um guia existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tópico criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Guia não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<TopicoResponseDTO> criar(
            @PathVariable @NonNull UUID guiaId,
            @Valid @RequestBody TopicoRequestDTO dto) {

        TopicoResponseDTO response = topicoService.criar(guiaId, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Atualizar tópico", description = "Atualiza um tópico existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tópico atualizado"),
            @ApiResponse(responseCode = "404", description = "Tópico não encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<TopicoResponseDTO> atualizar(
            @PathVariable @NonNull UUID id,
            @Valid @RequestBody TopicoRequestDTO dto) {

        return ResponseEntity.ok(topicoService.atualizar(id, dto));
    }

    @Operation(summary = "Excluir tópico", description = "Remove um tópico.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tópico removido"),
            @ApiResponse(responseCode = "404", description = "Tópico não encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> excluir(
            @PathVariable @NonNull UUID id) {

        topicoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}