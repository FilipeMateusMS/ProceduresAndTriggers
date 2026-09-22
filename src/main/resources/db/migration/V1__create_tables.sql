CREATE TABLE usuario
(
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    criado_em TIMESTAMP NOT NULL
);

CREATE TABLE conta
(
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    saldo NUMERIC(15, 2) NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP NOT NULL,
    CONSTRAINT fk_conta_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT ck_conta_saldo CHECK (saldo >= 0),
    CONSTRAINT ck_conta_status CHECK (status IN ('ATIVA', 'BLOQUEADA'))
);

CREATE TABLE transacao
(
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL,
    conta_origem_id BIGINT,
    conta_destino_id BIGINT,
    valor NUMERIC(15, 2) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_transacao_tipo CHECK (tipo IN ('DEPOSITO', 'DEBITO', 'TRANSFERENCIA')),
    CONSTRAINT ck_transacao_valor CHECK (valor > 0),
    CONSTRAINT fk_transacao_origem FOREIGN KEY (conta_origem_id) REFERENCES conta (id),
    CONSTRAINT fk_transacao_destino FOREIGN KEY (conta_destino_id) REFERENCES conta (id)
);

CREATE TABLE auditoria_saldo
(
    id BIGSERIAL PRIMARY KEY,
    conta_id BIGINT NOT NULL,
    saldo_anterior NUMERIC(15, 2) NOT NULL,
    saldo_novo NUMERIC(15, 2) NOT NULL,
    alterado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_auditoria_conta FOREIGN KEY (conta_id) REFERENCES conta (id)
);
