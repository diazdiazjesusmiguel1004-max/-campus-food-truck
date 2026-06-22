package com.campus.foodtruck.domain.ports.in;

import com.campus.foodtruck.domain.model.Producto;

import java.util.List;

public interface ProductoUseCase {
    List<Producto> obtenerTodos();
    Producto obtenerPorId(Long id);
    Producto guardar(Producto producto);
    void eliminar(Long id);
}
