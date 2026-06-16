package br.ifsp.studyhub_api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.ifsp.studyhub_api.dto.GuiaPutRequestDTO;
import br.ifsp.studyhub_api.dto.GuiaRequestDTO;
import br.ifsp.studyhub_api.dto.GuiaResponseDTO;
import br.ifsp.studyhub_api.exception.BusinessException;
import br.ifsp.studyhub_api.exception.ResourceNotFoundException;
import br.ifsp.studyhub_api.model.Guia;
import br.ifsp.studyhub_api.model.Sala;
import br.ifsp.studyhub_api.repository.GuiaRepository;
import br.ifsp.studyhub_api.repository.SalaRepository;

@Service
public class GuiaService {

    private final GuiaRepository guiaRepository;
    private final SalaRepository salaRepository;

    private final Path pastaUploads = Paths.get("uploads/materiais");

    public GuiaService(GuiaRepository guiaRepository, SalaRepository salaRepository) {
        this.guiaRepository = guiaRepository;
        this.salaRepository = salaRepository;
    }

    @Transactional
    public GuiaResponseDTO criar(@NonNull UUID salaId, GuiaRequestDTO dto) {
        return criarGuiaComArquivos(salaId, dto, null);
    }

    @Transactional
    public GuiaResponseDTO criarGuiaComArquivos(@NonNull UUID salaId, GuiaRequestDTO dto,
            List<MultipartFile> arquivos) {
        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new ResourceNotFoundException("Sala não encontrada com o ID: " + salaId));

        List<Guia.DadosTopico> topicosIniciais = dto.topicos().stream()
                .map(t -> new Guia.DadosTopico(null, t.titulo(), t.descricao()))
                .toList();

        Guia guia = new Guia(dto.titulo(), sala, topicosIniciais);

        if (arquivos != null && !arquivos.isEmpty()) {
            for (MultipartFile arquivo : arquivos) {
                String caminhoSalvo = armazenarArquivoNoDisco(arquivo);
                guia.addMaterial(caminhoSalvo);
            }
        }

        Guia guiaSalva = guiaRepository.save(guia);
        return new GuiaResponseDTO(guiaSalva);
    }

    @Transactional
    public GuiaResponseDTO atualizar(@NonNull UUID id, GuiaPutRequestDTO dto) {
        Guia guia = guiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guia não encontrado com o ID: " + id));

        List<Guia.DadosTopico> dadosNovosTopicos = dto.topicos().stream()
                .map(t -> new Guia.DadosTopico(t.id(), t.titulo(), t.descricao()))
                .toList();

        guia.atualizarEstrutura(dto.titulo(), dadosNovosTopicos);

        if (dto.materiais() != null) {
            guia.atualizarMateriais(dto.materiais());
        }

        Guia guiaAtualizado = guiaRepository.save(guia);
        return new GuiaResponseDTO(guiaAtualizado);
    }

    @Transactional
    public void excluir(@NonNull UUID id) {
        Guia guia = guiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guia não encontrado."));
        guiaRepository.delete(guia);
    }

    private String armazenarArquivoNoDisco(MultipartFile arquivo) {
        String nomeOriginal = arquivo.getOriginalFilename();
        if (nomeOriginal == null || arquivo.isEmpty()) {
            throw new BusinessException("O arquivo enviado está vazio ou corrompido.");
        }

        try {
            if (!Files.exists(pastaUploads)) {
                Files.createDirectories(pastaUploads);
            }

            String nomeUnico = UUID.randomUUID() + "_" + nomeOriginal.replaceAll("\\s+", "_");
            Path destinoCompleto = pastaUploads.resolve(nomeUnico);
            Files.copy(arquivo.getInputStream(), destinoCompleto, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/materiais/" + nomeUnico;

        } catch (IOException e) {
            throw new RuntimeException("Falha crítica de I/O ao armazenar anexo no servidor.", e);
        }
    }
}