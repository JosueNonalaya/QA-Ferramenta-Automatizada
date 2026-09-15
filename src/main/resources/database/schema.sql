CREATE TABLE IF NOT EXISTS usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    senha TEXT NOT NULL,
    telefone TEXT
);

CREATE TABLE IF NOT EXISTS auditoria (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome_projeto TEXT NOT NULL,
    data_hora_agendada TEXT,
    inicio TEXT,
    fim TEXT,
    status TEXT NOT NULL,
    percentual_aderencia_aceitavel REAL NOT NULL,
    percentual_aderencia REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS responsavel (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario_id INTEGER NOT NULL,
    tipo TEXT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS auditoria_responsavel (
    auditoria_id INTEGER NOT NULL,
    responsavel_id INTEGER NOT NULL,
    PRIMARY KEY (auditoria_id, responsavel_id),
    FOREIGN KEY (auditoria_id) REFERENCES auditoria(id),
    FOREIGN KEY (responsavel_id) REFERENCES responsavel(id)
);

CREATE TABLE IF NOT EXISTS documento (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    caminho TEXT NOT NULL,
    tipo TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS auditoria_documento (
    auditoria_id INTEGER NOT NULL,
    documento_id INTEGER NOT NULL,
    PRIMARY KEY (auditoria_id, documento_id),
    FOREIGN KEY (auditoria_id) REFERENCES auditoria(id),
    FOREIGN KEY (documento_id) REFERENCES documento(id)
);

CREATE TABLE IF NOT EXISTS checklist (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    auditoria_id INTEGER UNIQUE,
    FOREIGN KEY (auditoria_id) REFERENCES auditoria(id)
);

CREATE TABLE IF NOT EXISTS pergunta_checklist (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    descricao TEXT NOT NULL,
    ordem INTEGER NOT NULL,
    checklist_id INTEGER NOT NULL,
    FOREIGN KEY (checklist_id) REFERENCES checklist(id)
);

CREATE TABLE IF NOT EXISTS resposta_checklist (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    opcao TEXT NOT NULL,
    pergunta_id INTEGER NOT NULL,
    FOREIGN KEY (pergunta_id) REFERENCES pergunta_checklist(id)
);

CREATE TABLE IF NOT EXISTS classificacao_nc (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    descricao TEXT
);

CREATE TABLE IF NOT EXISTS nao_conformidade (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    descricao TEXT NOT NULL,
    status TEXT NOT NULL,
    classificacao_id INTEGER NOT NULL,
    auditoria_id INTEGER NOT NULL,
    FOREIGN KEY (classificacao_id) REFERENCES classificacao_nc(id),
    FOREIGN KEY (auditoria_id) REFERENCES auditoria(id)
);

CREATE TABLE IF NOT EXISTS notificacao (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    mensagem TEXT NOT NULL,
    data_hora TEXT NOT NULL,
    status TEXT NOT NULL,
    usuario_id INTEGER NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);