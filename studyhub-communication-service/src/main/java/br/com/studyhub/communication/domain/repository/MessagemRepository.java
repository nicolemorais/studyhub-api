package br.com.studyhub.communication.domain.repository;

import br.com.studyhub.communication.domain.model.Mensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessagemRepository extends JpaRepository<Mensagem, UUID> {
    Page<Mensagem> findBySalaIdAndConteudoTextoContainingIgnoreCase(UUID salaId, String keyword, Pageable pageable);

    Page<Mensagem> findBySalaId(UUID salaId, Pageable pageable);
}