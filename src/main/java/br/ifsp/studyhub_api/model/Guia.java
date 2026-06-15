package br.ifsp.studyhub_api.model;

import br.ifsp.studyhub_api.exception.BusinessException;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_guias")
public class Guia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @OneToMany(mappedBy = "guia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topico> topicos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "tb_guia_materiais", joinColumns = @JoinColumn(name = "guia_id"))
    @Column(name = "url_material")
    private List<String> materiais = new ArrayList<>();

    public record DadosTopico(UUID id, String titulo, String descricao) {
    }

    protected Guia() {
    }

    public Guia(String titulo, Sala sala, List<DadosTopico> topicosIniciais) {
        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("O título da guia é obrigatório.");
        }
        if (sala == null) {
            throw new BusinessException("A guia deve estar associada a uma sala de estudos válida.");
        }
        if (topicosIniciais == null || topicosIniciais.isEmpty()) {
            throw new BusinessException("A guia deve conter pelo menos 1 tópico.");
        }

        this.titulo = titulo;
        this.sala = sala;

        for (DadosTopico dado : topicosIniciais) {
            this.addTopico(dado.titulo(), dado.descricao());
        }
    }

    public void renomearTituloGuia(String novoTitulo) {
        if (novoTitulo != null) {
            if (novoTitulo.isBlank()) {
                throw new BusinessException(
                        "O título da guia não pode ser alterado para um valor vazio.");
            }
            this.titulo = novoTitulo;
        }
    }

    public void addTopico(String tituloTopico, String descricaoTopico) {
        Topico novoTopico = new Topico(tituloTopico, descricaoTopico, this);
        this.topicos.add(novoTopico);
    }

    public void atualizarEstrutura(String novoTitulo, List<DadosTopico> dadosNovosTopicos) {
        this.renomearTituloGuia(novoTitulo);

        if (dadosNovosTopicos == null) {
            return;
        }

        if (dadosNovosTopicos.isEmpty()) {
            throw new BusinessException("A guia deve conter pelo menos 1 tópico ativo.");
        }

        this.topicos.removeIf(topicoAntigo -> dadosNovosTopicos.stream()
                .noneMatch(novo -> novo.id() != null && novo.id().equals(topicoAntigo.getId())));

        for (DadosTopico dado : dadosNovosTopicos) {
            if (dado.id() != null) {
                this.topicos.stream()
                        .filter(t -> t.getId().equals(dado.id()))
                        .findFirst()
                        .ifPresent(t -> t.alterarConteudo(dado.titulo(), dado.descricao()));
            } else {
                this.addTopico(dado.titulo(), dado.descricao());
            }
        }

        if (this.topicos.isEmpty()) {
            throw new BusinessException("A guia deve conter pelo menos 1 tópico ativo.");
        }
    }

    public void addMaterial(String urlMaterial) {
        if (urlMaterial == null || urlMaterial.isBlank()) {
            throw new BusinessException("A URL do material não pode ser nula ou vazia.");
        }

        String urlLower = urlMaterial.toLowerCase().trim();
        if (urlLower.endsWith(".exe") || urlLower.endsWith(".bat")) {
            throw new BusinessException("Não é permitido o upload de arquivos executáveis.");
        }

        this.materiais.add(urlMaterial);
    }

    public void atualizarMateriais(List<String> novasUrls) {
        if (novasUrls == null)
            return;

        this.materiais.clear(); 
                            
        for (String url : novasUrls) {
            this.addMaterial(url); 
        }
    }

    public UUID getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Sala getSala() {
        return sala;
    }

    public List<Topico> getTopicos() {
        return new ArrayList<>(topicos);
    }

    public List<String> getMateriais() {
        return new ArrayList<>(materiais);
    }

}
