package br.ifsp.studyhub_api.service;

import br.ifsp.studyhub_api.model.Sala;
import br.ifsp.studyhub_api.repository.SalaRepository;
import br.ifsp.studyhub_api.dto.SalaRequestDTO;
import br.ifsp.studyhub_api.dto.SalaResponseDTO;
import br.ifsp.studyhub_api.exception.ResourceNotFoundException;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalaService {

    private final SalaRepository repository;

    public SalaService(SalaRepository repository){
        this.repository = repository;
    }

    @Transactional
    public SalaResponseDTO insert(SalaRequestDTO dto) {
        Sala entity = new Sala(dto.titulo(), dto.descricao());

        entity = repository.save(entity);
        return new SalaResponseDTO(entity);
    }

    @Transactional
    public void delete(UUID id){
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Sala não localizada para o ID: " + id);
        }
        
        repository.deleteById(id);
    }
}
