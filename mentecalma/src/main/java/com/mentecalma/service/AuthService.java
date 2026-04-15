package com.mentecalma.service;

import com.mentecalma.dto.request.LoginRequest;
import com.mentecalma.dto.request.RegisterRequest;
import com.mentecalma.dto.response.AuthResponse;
import com.mentecalma.model.Usuario;
import com.mentecalma.repository.UsuarioRepository;
import com.mentecalma.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registrar(RegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.email()))
            throw new IllegalArgumentException("El email ya está registrado.");

        var usuario = Usuario.builder()
                .nombre(req.nombre())
                .email(req.email())
                .passwordHash(passwordEncoder.encode(req.password()))
                .build();

        usuarioRepository.save(usuario);
        String token = jwtService.generarToken(usuario);
        return new AuthResponse(token, usuario.getEmail(), usuario.getNombre(), usuario.getRol().name());
    }

    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );
        var usuario = usuarioRepository.findByEmail(req.email()).orElseThrow();
        String token = jwtService.generarToken(usuario);
        return new AuthResponse(token, usuario.getEmail(), usuario.getNombre(), usuario.getRol().name());
    }
}