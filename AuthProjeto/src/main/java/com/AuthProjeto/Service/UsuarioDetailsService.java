
package com.AuthProjeto.Service;

import com.AuthProjeto.Entity.Usuario;
import com.AuthProjeto.Repository.UsuarioRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    "Usuário ou senha inválidos"
                )
            );

        if (usuario.getStatus() == null) {
            throw new IllegalStateException(
                "O usuário não possui um status definido."
            );
        }

        return User.builder()
            .username(usuario.getEmail())
            .password(usuario.getSenha())
            .authorities(usuario.getStatus().name())
            .build();
    }
}
