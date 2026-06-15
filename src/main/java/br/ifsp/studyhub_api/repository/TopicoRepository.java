package br.ifsp.studyhub_api.repository;

import br.ifsp.studyhub_api.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, UUID> {

}