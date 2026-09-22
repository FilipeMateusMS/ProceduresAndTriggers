package com.bank.procedures.repository;

import com.bank.procedures.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByUsuarioId(Long usuarioId);

    @Procedure(procedureName = "sp_depositar")
    void depositar(
            @Param("p_conta_id") Long contaId,
            @Param("p_valor") BigDecimal valor
    );

    @Procedure(procedureName = "sp_debitar")
    void debitar(
            @Param("p_conta_id") Long contaId,
            @Param("p_valor") BigDecimal valor
    );

    @Procedure(procedureName = "sp_transferir")
    void transferir(
            @Param("p_conta_origem_id") Long contaOrigemId,
            @Param("p_conta_destino_id") Long contaDestinoId,
            @Param("p_valor") BigDecimal valor
    );
}
