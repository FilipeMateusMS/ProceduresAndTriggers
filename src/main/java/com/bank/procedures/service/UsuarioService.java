package com.bank.procedures.service;

import com.bank.procedures.dto.CriarUsuarioRequest;
import com.bank.procedures.entity.Usuario;
import com.bank.procedures.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario criar(CriarUsuarioRequest request) {
        if (usuarioRepository.existsByCpf(request.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        return usuarioRepository.save(
                Usuario.builder()
                        .nome(request.nome())
                        .cpf(request.cpf())
                        .criadoEm(LocalDateTime.now())
                        .build()
        );
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }
}
