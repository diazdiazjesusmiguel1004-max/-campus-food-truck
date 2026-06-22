package com.campus.foodtruck.domain.ports.out;

import com.campus.foodtruck.domain.model.Usuario;

public interface TokenServicePort {
    String generarToken(Usuario usuario);
}
