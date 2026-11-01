package com.AuthProjeto.Service;

import com.AuthProjeto.Dtos.CadastroRequest;
import com.AuthProjeto.Entity.Usuario;
import com.AuthProjeto.Entity.Enum.StatusUser;
import com.AuthProjeto.Repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public CadastroService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void cadastrar(CadastroRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException(
                "Este e-mail já está cadastrado"
            );
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.email());
        usuario.setSenha(
            passwordEncoder.encode(request.senha())
        );
        usuario.setStatus(StatusUser.ROLE_USER);

        usuarioRepository.save(usuario);
    }
}