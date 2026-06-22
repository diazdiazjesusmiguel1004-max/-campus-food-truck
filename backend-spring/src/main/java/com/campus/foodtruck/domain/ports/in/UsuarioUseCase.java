package com.campus.foodtruck.domain.ports.in;

import com.campus.foodtruck.domain.model.Usuario;

public interface UsuarioUseCase {
    Usuario registrar(Usuario usuario);
    Usuario obtenerPorEmail(String email);
    String login(String email, String password);
}
