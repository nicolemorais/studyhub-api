-- 1. Tabela de Utilizadores (Espelho Local / Cache de Leitura alimentado pelo Kafka)
CREATE TABLE tb_usuarios (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

-- 2. Tabela de Salas de Estudo
CREATE TABLE tb_salas (
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    criada_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    professor_id UUID NOT NULL
);

-- 2.1. Tabela de Junção para Alunos (Reflete o @ElementCollection da Entidade Sala)
CREATE TABLE tb_salas_alunos (
    sala_id UUID NOT NULL,
    usuario_id UUID NOT NULL,
    PRIMARY KEY (sala_id, usuario_id),
    CONSTRAINT fk_sala_aluno FOREIGN KEY (sala_id) REFERENCES tb_salas(id) ON DELETE CASCADE
);

-- 3. Tabela de Guias de Estudo
CREATE TABLE tb_guias (
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    sala_id UUID NOT NULL,
    CONSTRAINT fk_guia_sala FOREIGN KEY (sala_id) REFERENCES tb_salas(id) ON DELETE CASCADE
);

-- 4. Tabela de Tópicos (Filhos das Guias de Estudo)
CREATE TABLE tb_topicos (
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    conteudo TEXT NOT NULL,
    ordem_exibicao INT NOT NULL,
    guia_id UUID NOT NULL,
    CONSTRAINT fk_topico_guia FOREIGN KEY (guia_id) REFERENCES tb_guias(id) ON DELETE CASCADE
);

-- 5. Tabela de Materiais (Filhos dos tópicos)
CREATE TABLE tb_materiais (
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    url_arquivo VARCHAR(512),
    topico_id UUID NOT NULL,
    CONSTRAINT fk_material_topico FOREIGN KEY (topico_id) REFERENCES tb_topicos(id) ON DELETE CASCADE
);