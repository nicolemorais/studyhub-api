CREATE TABLE tb_messages (
    id UUID PRIMARY KEY,
    room_id UUID NOT NULL,
    remetente_id UUID NOT NULL,
    conteudo_texto TEXT NOT NULL,
    url_anexo VARCHAR(512),
    enviada_em TIMESTAMP NOT NULL
);

-- Índice GIN exigido pelo critério de aceitação de Busca Paginada de alta performance
CREATE INDEX idx_messages_conteudo ON tb_messages USING gin(to_tsvector('portuguese', conteudo_texto));