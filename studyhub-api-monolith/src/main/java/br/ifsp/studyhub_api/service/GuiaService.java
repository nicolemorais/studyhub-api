package br.ifsp.studyhub_api.service;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ifsp.studyhub_api.dto.GuiaRequestDTO;
import br.ifsp.studyhub_api.dto.GuiaResponseDTO;
import br.ifsp.studyhub_api.exception.ResourceNotFoundException;
import br.ifsp.studyhub_api.model.Guia;
import br.ifsp.studyhub_api.repository.GuiaRepository;
import br.ifsp.studyhub_api.repository.SalaRepository;

@Service
public class GuiaService {

    private final GuiaRepository guiaRepository;
    private final SalaRepository salaRepository;

    public GuiaService(
            GuiaRepository guiaRepository,
            SalaRepository salaRepository) {

        this.guiaRepository = guiaRepository;
        this.salaRepository = salaRepository;
    }

    @SuppressWarnings("null")
    @Transactional
    public GuiaResponseDTO criar(GuiaRequestDTO dto) {

        salaRepository.findById(dto.salaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sala não encontrada com ID: " + dto.salaId()));

        Guia guia = new Guia(
                dto.titulo(),
                dto.descricao(),
                dto.salaId());

        guia = guiaRepository.save(guia);

        return GuiaResponseDTO.fromEntity(guia);
    }

    @Transactional(readOnly = true)
    public GuiaResponseDTO buscarPorId(@NonNull UUID id) {

        Guia guia = guiaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guia não encontrado."));

        return GuiaResponseDTO.fromEntity(guia);
    }

    @Transactional
    public GuiaResponseDTO atualizar(@NonNull UUID id, GuiaRequestDTO dto) {

        Guia guia = guiaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guia não encontrado."));

        guia.atualizar(
                dto.titulo(),
                dto.descricao());

        guia = guiaRepository.save(guia);

        return GuiaResponseDTO.fromEntity(guia);
    }

    @SuppressWarnings("null")
    @Transactional
    public void excluir(@NonNull UUID id) {

        Guia guia = guiaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guia não encontrado."));

        guiaRepository.delete(guia);
    }
}