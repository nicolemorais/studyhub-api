package br.ifsp.studyhub_api.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ifsp.studyhub_api.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, UUID> {
    
    Page<Sala> findByAlunosEmail(String email, Pageable pageable);

    Page<Sala> findByCriadorEmail(String email, Pageable pageable);
}
