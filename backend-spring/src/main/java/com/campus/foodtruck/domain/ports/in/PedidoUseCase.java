package com.campus.foodtruck.domain.ports.in;

import com.campus.foodtruck.domain.model.EstadoPedido;
import com.campus.foodtruck.domain.model.Pedido;

import java.util.List;
import java.util.Map;

public interface PedidoUseCase {
    Pedido crearPedido(Long usuarioId, List<Map<String, Object>> items);
    Pedido obtenerPorId(Long id);
    List<Pedido> obtenerTodos();
    List<Pedido> obtenerPorUsuario(Long usuarioId);
    Pedido actualizarEstado(Long id, EstadoPedido nuevoEstado);
    Map<String, Object> obtenerReporteAnalitico();
}
