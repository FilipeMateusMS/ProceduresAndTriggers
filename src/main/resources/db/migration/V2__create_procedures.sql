CREATE OR REPLACE PROCEDURE sp_depositar(
    IN p_conta_id BIGINT,
    IN p_valor NUMERIC(15, 2)
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_valor IS NULL OR p_valor <= 0 THEN
        RAISE EXCEPTION 'Valor do depósito deve ser maior que zero';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM conta
        WHERE id = p_conta_id AND status = 'ATIVA'
    ) THEN
        RAISE EXCEPTION 'Conta não encontrada ou não está ativa';
    END IF;

    UPDATE conta
       SET saldo = saldo + p_valor
     WHERE id = p_conta_id;

    INSERT INTO transacao(tipo, conta_destino_id, valor)
    VALUES ('DEPOSITO', p_conta_id, p_valor);
END;
$$;

CREATE OR REPLACE PROCEDURE sp_debitar(
    IN p_conta_id BIGINT,
    IN p_valor NUMERIC(15, 2)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_saldo NUMERIC(15, 2);
BEGIN
    IF p_valor IS NULL OR p_valor <= 0 THEN
        RAISE EXCEPTION 'Valor do débito deve ser maior que zero';
    END IF;

    SELECT saldo INTO v_saldo
      FROM conta
     WHERE id = p_conta_id
       AND status = 'ATIVA'
     FOR UPDATE;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Conta não encontrada ou não está ativa';
    END IF;

    IF v_saldo < p_valor THEN
        RAISE EXCEPTION 'Saldo insuficiente';
    END IF;

    UPDATE conta
       SET saldo = saldo - p_valor
     WHERE id = p_conta_id;

    INSERT INTO transacao(tipo, conta_origem_id, valor)
    VALUES ('DEBITO', p_conta_id, p_valor);
END;
$$;

CREATE OR REPLACE PROCEDURE sp_transferir(
    IN p_conta_origem_id BIGINT,
    IN p_conta_destino_id BIGINT,
    IN p_valor NUMERIC(15, 2)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_saldo_origem NUMERIC(15, 2);
    v_status_origem VARCHAR(20);
    v_status_destino VARCHAR(20);
BEGIN
    IF p_valor IS NULL OR p_valor <= 0 THEN
        RAISE EXCEPTION 'Valor da transferência deve ser maior que zero';
    END IF;

    IF p_conta_origem_id = p_conta_destino_id THEN
        RAISE EXCEPTION 'A conta de origem e destino devem ser diferentes';
    END IF;

    SELECT status INTO v_status_origem
      FROM conta
     WHERE id = p_conta_origem_id
     FOR UPDATE;

    IF NOT FOUND OR v_status_origem <> 'ATIVA' THEN
        RAISE EXCEPTION 'Conta de origem não encontrada ou não está ativa';
    END IF;

    SELECT status INTO v_status_destino
      FROM conta
     WHERE id = p_conta_destino_id
     FOR UPDATE;

    IF NOT FOUND OR v_status_destino <> 'ATIVA' THEN
        RAISE EXCEPTION 'Conta de destino não encontrada ou não está ativa';
    END IF;

    SELECT saldo INTO v_saldo_origem
      FROM conta
     WHERE id = p_conta_origem_id;

    IF v_saldo_origem < p_valor THEN
        RAISE EXCEPTION 'Saldo insuficiente';
    END IF;

    UPDATE conta SET saldo = saldo - p_valor
     WHERE id = p_conta_origem_id;

    UPDATE conta SET saldo = saldo + p_valor
     WHERE id = p_conta_destino_id;

    INSERT INTO transacao(tipo, conta_origem_id, conta_destino_id, valor)
    VALUES ('TRANSFERENCIA', p_conta_origem_id, p_conta_destino_id, p_valor);
END;
$$;
