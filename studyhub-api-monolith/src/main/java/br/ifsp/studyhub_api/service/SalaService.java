package br.ifsp.studyhub_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ifsp.studyhub_api.exception.ResourceNotFoundException;
import br.ifsp.studyhub_api.model.Sala;
import br.ifsp.studyhub_api.model.Usuario;
import br.ifsp.studyhub_api.repository.SalaRepository;
import br.ifsp.studyhub_api.repository.UsuarioRepository;

@Service
public class SalaService {

    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository; // Tabela espelho alimentada via Kafka

    public SalaService(SalaRepository salaRepository, UsuarioRepository usuarioRepository) {
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Regra de Negócio: Cria uma nova sala de estudos vinculada ao UUID do
     * Professor.
     */
    @Transactional
    public Sala criarSala(String titulo, String descricao, UUID professorId) {
        Sala novaSala = new Sala(titulo, descricao, professorId);
        return salaRepository.save(novaSala);
    }

    /**
     * Regra de Negócio: Permite vincular um aluno à sala diretamente pelo seu
     * e-mail.
     * Resolve o e-mail na tabela espelho local para extrair o UUID antes de
     * persistir.
     */
    @Transactional
    public void vincularAlunoPorEmail(@NonNull UUID salaId, String emailAluno) {
        // 1. Resolve o e-mail descobrindo o UUID do aluno na tabela espelho
        Usuario aluno = usuarioRepository.findByEmail(emailAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum aluno encontrado com o e-mail informado."));

        // 2. Busca a sala alvo
        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new ResourceNotFoundException("Sala de estudos não encontrada."));

        sala.adicionarAluno(aluno.getId());
        salaRepository.save(sala);
    }

    /**
     * Regra de Negócio: Desvincula um aluno da sala usando diretamente o UUID dele.
     */
    @Transactional
    public void desvincularAluno(UUID salaId, UUID alunoId) {
        @SuppressWarnings("null")
        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new ResourceNotFoundException("Sala de estudos não encontrada."));

        sala.removerAluno(alunoId);
        salaRepository.save(sala);
    }

    /**
     * Visão do Professor: Delega ao repositório a busca de salas
     * criadas exclusivamente pelo UUID do professor logado.
     */
    @Transactional(readOnly = true)
    public List<Sala> listarSalasPorProfessor(UUID professorId) {
        return salaRepository.findByProfessorId(professorId);
    }

    /**
     * Visão do Aluno: Delega ao repositório a busca (via JOIN na ElementCollection)
     * de todas as salas onde o UUID do aluno está matriculado.
     */
    @Transactional(readOnly = true)
    public List<Sala> listarSalasPorAluno(UUID alunoId) {
        return salaRepository.findByAlunoMatriculado(alunoId);
    }

}