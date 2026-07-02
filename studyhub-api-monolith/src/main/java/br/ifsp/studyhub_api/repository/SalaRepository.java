package br.ifsp.studyhub_api.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ifsp.studyhub_api.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, UUID> {

    // Visão do Professor: Busca salas cujo UUID do criador coincide
    List<Sala> findByProfessorId(UUID professorId);

    // Visão do Aluno: Busca salas onde o UUID do aluno está contido na ElementCollection
    @Query("SELECT s FROM Sala s JOIN s.alunosIds a WHERE a = :alunoId")
    List<Sala> findByAlunoMatriculado(@Param("alunoId") UUID alunoId);
}
