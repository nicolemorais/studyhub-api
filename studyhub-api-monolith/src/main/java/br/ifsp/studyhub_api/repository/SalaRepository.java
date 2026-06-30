package br.ifsp.studyhub_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ifsp.studyhub_api.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, UUID> {

    Page<Sala> findByAlunosEmail(String email, Pageable pageable);

    Page<Sala> findByProfessorEmail(String email, Pageable pageable);

    @Query("SELECT DISTINCT s FROM Sala s LEFT JOIN FETCH s.alunos WHERE s.professor.email = :email")
    List<Sala> buscarSalasEProfessorComAlunos(@Param("email") String email);

    @Query("SELECT DISTINCT s FROM Sala s JOIN s.alunos a LEFT JOIN FETCH s.alunos WHERE a.email = :email")
    List<Sala> buscarSalasEAlunosPorAlunoEmail(@Param("email") String email);
}
