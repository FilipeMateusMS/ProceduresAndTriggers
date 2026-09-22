package com.bank.procedures.service;

import com.bank.procedures.entity.Conta;
import com.bank.procedures.entity.Usuario;
import com.bank.procedures.repository.ContaRepository;
import com.bank.procedures.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ContaService {
    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Conta criar(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (contaRepository.findByUsuarioId(usuarioId).isPresent()) {
            throw new IllegalArgumentException("Usuário já possui uma conta");
        }

        LocalDateTime agora = LocalDateTime.now();

        return contaRepository.save(
                Conta.builder()
                        .usuario(usuario)
                        .saldo(BigDecimal.ZERO)
                        .status("ATIVA")
                        .criadoEm(agora)
                        .atualizadoEm(agora)
                        .build()
        );
    }

    public Conta buscar(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
    }
}
