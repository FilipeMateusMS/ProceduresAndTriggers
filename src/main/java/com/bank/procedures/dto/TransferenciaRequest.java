package com.bank.procedures.dto;

import java.math.BigDecimal;

public record TransferenciaRequest(
        Long contaOrigemId,
        Long contaDestinoId,
        BigDecimal valor
) {
}
