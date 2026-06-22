CREATE TABLE tb_usuarios (
    id UUID OLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAPRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(30) NOT NULL,
    ativo BOINT chk_perfil CHECK (perfil IN ('PROFESSOR', 'ALUNO'))
);

-- Inserindo um usuário administrador padrão
INSERT INTO tb_usuarios (email, senha, perfil, ativo) 
VALUES ('admin@studyhub.com', '$2a$12$A7H1/V5D2.Z.i9x5tN.m7.z2K2K2K2K2K2K2K2K2K2K2K2K2K2K2K', 'PROFESSOR', true);