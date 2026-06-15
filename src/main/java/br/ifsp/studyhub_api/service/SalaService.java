package br.ifsp.studyhub_api.service;

import br.ifsp.studyhub_api.model.Sala;
import br.ifsp.studyhub_api.model.Usuario;
import br.ifsp.studyhub_api.repository.SalaRepository;
import br.ifsp.studyhub_api.repository.UsuarioRepository;
import br.ifsp.studyhub_api.dto.SalaRequestDTO;
import br.ifsp.studyhub_api.dto.SalaResponseDTO;
import br.ifsp.studyhub_api.exception.BusinessException;
import br.ifsp.studyhub_api.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.lang.NonNull;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalaService {

    private final SalaRepository repository;
    private final UsuarioRepository usuarioRepository;

    public SalaService(SalaRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public Page<SalaResponseDTO> findAll(Pageable pageable) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailLogado = authentication.getName();

        boolean isAluno = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ALUNO"));

        if (isAluno) {
            return repository.findByAlunosEmail(emailLogado, pageable)
                    .map(sala -> new SalaResponseDTO(sala));
        }

        return repository.findByCriadorEmail(emailLogado, pageable)
                .map(sala -> new SalaResponseDTO(sala));
    }

    @Transactional
    public SalaResponseDTO insert(SalaRequestDTO dto) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailProfessor = authentication.getName();

        Usuario professor = usuarioRepository.findByEmail(emailProfessor)
                .orElseThrow(() -> new BusinessException("Professor não encontrado no sistema."));

        Sala sala = new Sala(dto.titulo(), dto.descricao(), professor);
        sala = repository.save(sala);

        return new SalaResponseDTO(sala);
    }

    @Transactional
    public void delete(@NonNull UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Sala não localizada para o ID: " + id);
        }

        repository.deleteById(id);
    }
}
