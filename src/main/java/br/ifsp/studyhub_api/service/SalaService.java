package br.ifsp.studyhub_api.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ifsp.studyhub_api.dto.SalaRequestDTO;
import br.ifsp.studyhub_api.dto.SalaResponseDTO;
import br.ifsp.studyhub_api.exception.BusinessException;
import br.ifsp.studyhub_api.exception.ResourceNotFoundException;
import br.ifsp.studyhub_api.model.Sala;
import br.ifsp.studyhub_api.model.Usuario;
import br.ifsp.studyhub_api.repository.SalaRepository;
import br.ifsp.studyhub_api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class SalaService {

    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;

    public SalaService(SalaRepository salaRepository, UsuarioRepository usuarioRepository) {
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Page<SalaResponseDTO> findAll(Pageable pageable) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailLogado = authentication.getName();

        boolean isAluno = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ALUNO"));

        if (isAluno) {
            return salaRepository.findByAlunosEmail(emailLogado, pageable)
                    .map(sala -> new SalaResponseDTO(sala));
        }

        return salaRepository.findByCriadorEmail(emailLogado, pageable)
                .map(sala -> new SalaResponseDTO(sala));
    }

    @Transactional
    public SalaResponseDTO insert(SalaRequestDTO dto) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailProfessor = authentication.getName();

        Usuario professor = usuarioRepository.findByEmail(emailProfessor)
                .orElseThrow(() -> new BusinessException("Professor não encontrado no sistema."));

        Sala sala = new Sala(dto.titulo(), dto.descricao(), professor);
        sala = salaRepository.save(sala);

        return new SalaResponseDTO(sala);
    }

    @Transactional
    public void delete(@NonNull UUID id) {
        if (!salaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sala não localizada para o ID: " + id);
        }

        salaRepository.deleteById(id);
    }

    @Transactional
    public void matricularAluno(@NonNull UUID salaId, @NonNull UUID alunoId) {
        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada."));

        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        sala.adicionarAluno(aluno);
        salaRepository.save(sala);
    }

    @Transactional
    public void removerAluno(@NonNull UUID salaId, @NonNull UUID alunoId) {
        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada"));


        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        sala.removerAluno(aluno);
        salaRepository.save(sala);
    }
}
