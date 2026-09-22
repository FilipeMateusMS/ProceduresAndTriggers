package com.bank.procedures.controller;

import com.bank.procedures.entity.Conta;
import com.bank.procedures.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contas")
@RequiredArgsConstructor
public class ContaController {
    private final ContaService contaService;

    @PostMapping("/usuario/{usuarioId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Conta criar(@PathVariable Long usuarioId) {
        return contaService.criar(usuarioId);
    }

    @GetMapping("/{id}")
    public Conta buscar(@PathVariable Long id) {
        return contaService.buscar(id);
    }
}
