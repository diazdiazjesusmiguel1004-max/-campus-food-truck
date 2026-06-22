package com.campus.foodtruck.domain.ports.out;

import com.campus.foodtruck.domain.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepositoryPort {
    Pedido guardar(Pedido pedido);
    Optional<Pedido> buscarPorId(Long id);
    List<Pedido> buscarTodos();
    List<Pedido> buscarPorUsuarioId(Long usuarioId);
}
