package br.ifsp.studyhub_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ifsp.studyhub_api.dto.GuiaPutRequestDTO;
import br.ifsp.studyhub_api.dto.GuiaRequestDTO;
import br.ifsp.studyhub_api.dto.GuiaResponseDTO;
import br.ifsp.studyhub_api.service.GuiaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/guias")
public class GuiaController {

    private final GuiaService guiaService;
    private final ObjectMapper objectMapper;

    public GuiaController(GuiaService guiaService, ObjectMapper objectMapper) {
        this.guiaService = guiaService;
        this.objectMapper = objectMapper;
    }

    /**
     * Endpoint para criar um guia de estudos com tópicos e anexos de arquivos físicos.
     * Rota completa: POST /api/v1/guias/sala/{salaId}
     */
    @PostMapping(value = "/sala/{salaId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<GuiaResponseDTO> criar(
            @PathVariable @NonNull UUID salaId,
            @RequestParam("guia") String guiaJson,
            @RequestParam(value = "arquivos", required = false) List<MultipartFile> arquivos) throws Exception {
        
        GuiaRequestDTO dto = objectMapper.readValue(guiaJson, GuiaRequestDTO.class);
        
        GuiaResponseDTO response = guiaService.criarGuiaComArquivos(salaId, dto, arquivos);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para atualizar completamente ou parcialmente o guia, tópicos e mídias.
     * Rota completa: PUT /api/v1/guias/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<GuiaResponseDTO> atualizar(
            @PathVariable @NonNull UUID id,
            @Valid @RequestBody GuiaPutRequestDTO dto) {
      
        GuiaResponseDTO response = guiaService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para deletar o guia e limpar o banco.
     * Rota completa: DELETE /api/v1/guias/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> excluir(@PathVariable @NonNull UUID id) {
      
        guiaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}