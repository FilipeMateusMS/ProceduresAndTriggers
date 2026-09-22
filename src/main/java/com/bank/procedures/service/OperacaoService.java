package com.bank.procedures.service;

import com.bank.procedures.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OperacaoService {
    private final ContaRepository contaRepository;

    @Transactional
    public void depositar(Long contaId, BigDecimal valor) {
        contaRepository.depositar(contaId, valor);
    }

    @Transactional
    public void debitar(Long contaId, BigDecimal valor) {
        contaRepository.debitar(contaId, valor);
    }

    @Transactional
    public void transferir(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) {
        contaRepository.transferir(contaOrigemId, contaDestinoId, valor);
    }
}
