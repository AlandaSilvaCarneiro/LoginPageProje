package com.AuthProjeto.Controller;

import com.AuthProjeto.Dtos.AuthResponse;
import com.AuthProjeto.Dtos.CadastroRequest;
import com.AuthProjeto.Dtos.LoginRequest;
import com.AuthProjeto.Service.CadastroService;
import com.AuthProjeto.Service.JwtService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final CadastroService cadastroService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            CadastroService cadastroService,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.cadastroService = cadastroService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/cadastro")
    public ResponseEntity<Map<String, String>> cadastrar(
            @Valid @RequestBody CadastroRequest request) {

        cadastroService.cadastrar(request);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of(
                "mensagem", "Usuário cadastrado com sucesso"
            ));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        Authentication autenticacao =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.senha()
                )
            );

        UserDetails usuario =
            (UserDetails) autenticacao.getPrincipal();

        String token = jwtService.gerarToken(usuario);

        return ResponseEntity.ok(
            new AuthResponse(token, "Bearer")
        );
    }

    @GetMapping("/privado")
    public Map<String, String> privado() {
        return Map.of(
            "mensagem", "Você está autenticado com JWT!"
        );
    }

    @GetMapping("/admin/status")
    public Map<String, String> admin() {
        return Map.of(
            "mensagem", "Acesso administrativo autorizado"
        );
    }

    @GetMapping("/publico/status")
    public Map<String, String> publico() {
        return Map.of(
            "mensagem", "API funcionando"
        );
    }
}