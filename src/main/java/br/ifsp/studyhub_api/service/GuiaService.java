package br.ifsp.studyhub_api.service;

import br.ifsp.studyhub_api.dto.*;
import br.ifsp.studyhub_api.exception.ResourceNotFoundException;
import br.ifsp.studyhub_api.model.Guia;
import br.ifsp.studyhub_api.model.Sala;
import br.ifsp.studyhub_api.repository.GuiaRepository;
import br.ifsp.studyhub_api.repository.SalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;

@Service
public class GuiaService {

    private final GuiaRepository guiaRepository;
    private final SalaRepository salaRepository;

    public GuiaService(GuiaRepository guiaRepository, SalaRepository salaRepository) {
        this.guiaRepository = guiaRepository;
        this.salaRepository = salaRepository;
    }

    /**
     * Cria uma nova guia associada a uma sala.
     */
    @Transactional
    public GuiaResponseDTO criar(@NonNull UUID salaId, GuiaRequestDTO dto) {

        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new ResourceNotFoundException("Sala de estudos não encontrada com o ID: " + salaId));

        List<Guia.DadosTopico> topicosIniciais = dto.topicos().stream()
                .map(t -> new Guia.DadosTopico(null, t.titulo(), t.descricao()))
                .toList();

        Guia guia = new Guia(dto.titulo(), sala, topicosIniciais);

        if (dto.materiais() != null) {
            dto.materiais().forEach(guia::addMaterial);
        }

        return new GuiaResponseDTO(guiaRepository.save(guia));
    }

    /**
     * Atualiza a ementa, tópicos e materiais de um guia existente.
     */
    @Transactional
    public GuiaResponseDTO atualizar(@NonNull UUID id, GuiaPutRequestDTO dto) {
        Guia guia = guiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guia de estudo não encontrado com o ID: " + id));
        
        List<Guia.DadosTopico> dadosTopicos = dto.topicos().stream()
                .map(t -> new Guia.DadosTopico(t.id(), t.titulo(), t.descricao()))
                .toList();

        guia.atualizarEstrutura(dto.titulo(), dadosTopicos);
        guia.atualizarMateriais(dto.materiais());

        return new GuiaResponseDTO(guiaRepository.save(guia));
    }

    /**
     * Exclui o guia e todas as suas dependências.
     */
    @Transactional
    public void excluir(@NonNull UUID id) {

        if (!guiaRepository.existsById(id)){
            throw new ResourceNotFoundException("Guia não localizada para o ID: " + id);
        }
        guiaRepository.deleteById(id);
    }
}