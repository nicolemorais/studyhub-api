package br.ifsp.studyhub_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ifsp.studyhub_api.model.Guia;

@Repository
public interface GuiaRepository extends JpaRepository<Guia, UUID> {
    // Permite listar todas as guias publicadas especificamente dentro de uma Sala
    List<Guia> findBySalaId(UUID salaId);
}

