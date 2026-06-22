package com.campus.foodtruck.domain.ports.out;

import com.campus.foodtruck.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepositoryPort {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);
    Optional<Usuario> buscarPorId(Long id);
}
