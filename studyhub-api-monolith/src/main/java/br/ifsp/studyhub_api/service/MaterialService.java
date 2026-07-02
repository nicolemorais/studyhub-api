package br.ifsp.studyhub_api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ifsp.studyhub_api.dto.MaterialRequestDTO;
import br.ifsp.studyhub_api.dto.MaterialResponseDTO;
import br.ifsp.studyhub_api.exception.ResourceNotFoundException;
import br.ifsp.studyhub_api.model.Material;
import br.ifsp.studyhub_api.model.Topico;
import br.ifsp.studyhub_api.repository.MaterialRepository;
import br.ifsp.studyhub_api.repository.TopicoRepository;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final TopicoRepository topicoRepository;

    public MaterialService(
            MaterialRepository materialRepository,
            TopicoRepository topicoRepository) {

        this.materialRepository = materialRepository;
        this.topicoRepository = topicoRepository;
    }

    @Transactional
    public MaterialResponseDTO criar(UUID topicoId, MaterialRequestDTO dto) {

        @SuppressWarnings("null")
        Topico topico = topicoRepository.findById(topicoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tópico não encontrado."));

        Material material = new Material(
                dto.titulo(),
                dto.urlArquivo(),
                topico);

        topico.adicionarMaterial(material);

        material = materialRepository.save(material);

        return MaterialResponseDTO.fromEntity(material);
    }

    @Transactional
    public MaterialResponseDTO atualizar(UUID id, MaterialRequestDTO dto) {

        @SuppressWarnings("null")
        Material material = materialRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Material não encontrado."));

        material.atualizar(
                dto.titulo(),
                dto.urlArquivo());

        material = materialRepository.save(material);

        return MaterialResponseDTO.fromEntity(material);
    }

    @Transactional
    public void excluir(UUID id) {

        @SuppressWarnings("null")
        Material material = materialRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Material não encontrado."));

        Topico topico = material.getTopico();

        topico.removerMaterial(id);

        topicoRepository.save(topico);
    }
}