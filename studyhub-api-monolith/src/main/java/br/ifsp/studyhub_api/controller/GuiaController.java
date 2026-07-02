package br.ifsp.studyhub_api.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.studyhub_api.dto.GuiaRequestDTO;
import br.ifsp.studyhub_api.dto.GuiaResponseDTO;
import br.ifsp.studyhub_api.service.GuiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/guias")
@Validated
@Tag(name = "Guias", description = "Operações relacionadas aos guias de estudo")
public class GuiaController {

    private final GuiaService guiaService;

    public GuiaController(GuiaService guiaService) {
        this.guiaService = guiaService;
    }

    @Operation(summary = "Criar guia", description = "Cria um novo guia de estudos associado a uma sala.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Guia criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<GuiaResponseDTO> criar(
            @Valid @RequestBody GuiaRequestDTO dto) {

        GuiaResponseDTO response = guiaService.criar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Buscar guia", description = "Retorna um guia pelo seu identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Guia encontrado"),
            @ApiResponse(responseCode = "404", description = "Guia não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GuiaResponseDTO> buscarPorId(
            @PathVariable @NonNull UUID id) {

        return ResponseEntity.ok(guiaService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar guia", description = "Atualiza os dados de um guia existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Guia atualizado"),
            @ApiResponse(responseCode = "404", description = "Guia não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<GuiaResponseDTO> atualizar(
            @PathVariable @NonNull UUID id,
            @Valid @RequestBody GuiaRequestDTO dto) {

        GuiaResponseDTO response = guiaService.atualizar(id, dto);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Excluir guia", description = "Remove um guia de estudos.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Guia removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Guia não encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> excluir(
            @PathVariable @NonNull UUID id) {

        guiaService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}