package br.ifsp.studyhub_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ifsp.studyhub_api.model.Topico;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, UUID> {

}