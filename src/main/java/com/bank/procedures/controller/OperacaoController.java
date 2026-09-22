package com.bank.procedures.controller;

import com.bank.procedures.dto.OperacaoRequest;
import com.bank.procedures.dto.TransferenciaRequest;
import com.bank.procedures.entity.Conta;
import com.bank.procedures.service.ContaService;
import com.bank.procedures.service.OperacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operacoes")
@RequiredArgsConstructor
public class OperacaoController {
    private final OperacaoService operacaoService;
    private final ContaService contaService;

    @PostMapping("/depositar")
    public void depositar(@RequestBody OperacaoRequest request) {
        operacaoService.depositar(request.contaId(), request.valor());
    }

    @PostMapping("/debitar")
    public void debitar(@RequestBody OperacaoRequest request) {
        operacaoService.debitar(request.contaId(), request.valor());
    }

    @PostMapping("/transferir")
    public void transferir(@RequestBody TransferenciaRequest request) {
        operacaoService.transferir(
                request.contaOrigemId(),
                request.contaDestinoId(),
                request.valor()
        );
    }

    @GetMapping("/contas/{contaId}")
    public Conta consultarConta(@PathVariable Long contaId) {
        return contaService.buscar(contaId);
    }
}
