package br.ifsp.studyhub_api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ifsp.studyhub_api.dto.TopicoRequestDTO;
import br.ifsp.studyhub_api.dto.TopicoResponseDTO;
import br.ifsp.studyhub_api.exception.ResourceNotFoundException;
import br.ifsp.studyhub_api.model.Guia;
import br.ifsp.studyhub_api.model.Topico;
import br.ifsp.studyhub_api.repository.GuiaRepository;
import br.ifsp.studyhub_api.repository.TopicoRepository;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;
    private final GuiaRepository guiaRepository;

    public TopicoService(
            TopicoRepository topicoRepository,
            GuiaRepository guiaRepository) {

        this.topicoRepository = topicoRepository;
        this.guiaRepository = guiaRepository;
    }

    @Transactional
    public TopicoResponseDTO criar(UUID guiaId, TopicoRequestDTO dto) {

        @SuppressWarnings("null")
        Guia guia = guiaRepository.findById(guiaId)
                .orElseThrow(() -> new ResourceNotFoundException("Guia não encontrado."));

        Topico topico = new Topico(
                dto.titulo(),
                dto.conteudo(),
                dto.ordemExibicao(),
                guia);

        guia.adicionarTopico(topico);

        topico = topicoRepository.save(topico);

        return TopicoResponseDTO.fromEntity(topico);
    }

    @Transactional
    public TopicoResponseDTO atualizar(UUID id, TopicoRequestDTO dto) {

        @SuppressWarnings("null")
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tópico não encontrado."));

        topico.atualizar(
                dto.titulo(),
                dto.conteudo(),
                dto.ordemExibicao());

        topico = topicoRepository.save(topico);

        return TopicoResponseDTO.fromEntity(topico);
    }

    @Transactional
    public void excluir(UUID id) {

        @SuppressWarnings("null")
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tópico não encontrado."));

        Guia guia = topico.getGuia();

        guia.removerTopico(id);

        guiaRepository.save(guia);
    }
}