package br.ifsp.studyhub_api.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ifsp.studyhub_api.model.Guia;

@Repository
public interface GuiaRepository extends JpaRepository<Guia, UUID> {
  
    Page<Guia> findBySalaId(UUID salaId, Pageable pageable);
}