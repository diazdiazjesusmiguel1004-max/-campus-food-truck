package com.campus.foodtruck.application.service;

import com.campus.foodtruck.domain.model.Usuario;
import com.campus.foodtruck.domain.ports.in.UsuarioUseCase;
import com.campus.foodtruck.domain.ports.out.PasswordEncoderPort;
import com.campus.foodtruck.domain.ports.out.TokenServicePort;
import com.campus.foodtruck.domain.ports.out.UsuarioRepositoryPort;

public class UsuarioService implements UsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenServicePort tokenService;

    public UsuarioService(UsuarioRepositoryPort usuarioRepository, PasswordEncoderPort passwordEncoder, TokenServicePort tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.buscarPorEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya esta registrado");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.guardar(usuario);
    }

    @Override
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public String login(String email, String password) {
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales invalidas"));
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Credenciales invalidas");
        }
        return tokenService.generarToken(usuario);
    }
}
