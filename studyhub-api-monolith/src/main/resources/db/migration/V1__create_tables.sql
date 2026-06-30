-- 1. Tabela de Utilizadores(Apenas como reflexo local para relacionamentos)
CREATE TABLE tb_usuarios (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

-- 2. Tabela de Salas de Estudo
CREATE TABLE tb_salas (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    criada_em TIMESTAMP NOT NULL,
    professor_id UUID NOT NULL,
    CONSTRAINT fk_sala_professor FOREIGN KEY (professor_id) REFERENCES tb_usuarios(id)
);

-- 3. Tabela de Guias de Estudo
CREATE TABLE tb_guias (
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    conteudo TEXT,
    sala_id UUID NOT NULL,
    CONSTRAINT fk_guia_sala FOREIGN KEY (sala_id) REFERENCES tb_salas(id)
);