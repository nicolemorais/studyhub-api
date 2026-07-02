package br.ifsp.studyhub_api.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.studyhub_api.dto.MaterialRequestDTO;
import br.ifsp.studyhub_api.dto.MaterialResponseDTO;
import br.ifsp.studyhub_api.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/topicos/{topicoId}/materiais")
@Validated
@Tag(name = "Materiais", description = "Operações relacionadas aos materiais de estudo")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @Operation(
            summary = "Criar material",
            description = "Cria um novo material para um tópico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Material criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tópico não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<MaterialResponseDTO> criar(
            @PathVariable @NonNull UUID topicoId,
            @Valid @RequestBody MaterialRequestDTO dto) {

        MaterialResponseDTO response = materialService.criar(topicoId, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Atualizar material",
            description = "Atualiza um material existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material atualizado"),
            @ApiResponse(responseCode = "404", description = "Material não encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<MaterialResponseDTO> atualizar(
            @PathVariable @NonNull UUID id,
            @Valid @RequestBody MaterialRequestDTO dto) {

        return ResponseEntity.ok(materialService.atualizar(id, dto));
    }

    @Operation(
            summary = "Excluir material",
            description = "Remove um material."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Material removido"),
            @ApiResponse(responseCode = "404", description = "Material não encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> excluir(
            @PathVariable @NonNull UUID id) {

        materialService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}