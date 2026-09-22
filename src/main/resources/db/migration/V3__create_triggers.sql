CREATE OR REPLACE FUNCTION fn_auditar_saldo()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF OLD.saldo IS DISTINCT FROM NEW.saldo THEN
        INSERT INTO auditoria_saldo(
            conta_id,
            saldo_anterior,
            saldo_novo
        )
        VALUES (
            NEW.id,
            OLD.saldo,
            NEW.saldo
        );
    END IF;

    NEW.atualizado_em = CURRENT_TIMESTAMP;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_conta_auditoria_saldo
BEFORE UPDATE OF saldo ON conta
FOR EACH ROW
EXECUTE FUNCTION fn_auditar_saldo();
