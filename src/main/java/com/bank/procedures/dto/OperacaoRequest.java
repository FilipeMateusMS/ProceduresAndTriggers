package com.bank.procedures.dto;

import java.math.BigDecimal;

public record OperacaoRequest(Long contaId, BigDecimal valor) {
}
