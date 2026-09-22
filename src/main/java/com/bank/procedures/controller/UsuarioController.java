package com.bank.procedures.controller;

import com.bank.procedures.dto.CriarUsuarioRequest;
import com.bank.procedures.entity.Conta;
import com.bank.procedures.entity.Usuario;
import com.bank.procedures.service.ContaService;
import com.bank.procedures.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final ContaService contaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario criar(@RequestBody CriarUsuarioRequest request) {
        return usuarioService.criar(request);
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable Long id) {
        return usuarioService.buscar(id);
    }
}
