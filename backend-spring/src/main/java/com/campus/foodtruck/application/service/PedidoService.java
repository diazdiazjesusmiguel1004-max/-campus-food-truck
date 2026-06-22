package com.campus.foodtruck.application.service;

import com.campus.foodtruck.domain.model.DetallePedido;
import com.campus.foodtruck.domain.model.EstadoPedido;
import com.campus.foodtruck.domain.model.Pedido;
import com.campus.foodtruck.domain.model.Producto;
import com.campus.foodtruck.domain.model.Usuario;
import com.campus.foodtruck.domain.ports.in.PedidoUseCase;
import com.campus.foodtruck.domain.ports.out.AnalyticsClientPort;
import com.campus.foodtruck.domain.ports.out.PedidoRepositoryPort;
import com.campus.foodtruck.domain.ports.out.ProductoRepositoryPort;
import com.campus.foodtruck.domain.ports.out.UsuarioRepositoryPort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PedidoService implements PedidoUseCase {

    private final PedidoRepositoryPort pedidoRepository;
    private final UsuarioRepositoryPort usuarioRepository;
    private final ProductoRepositoryPort productoRepository;
    private final AnalyticsClientPort analyticsClient;

    public PedidoService(PedidoRepositoryPort pedidoRepository,
                         UsuarioRepositoryPort usuarioRepository,
                         ProductoRepositoryPort productoRepository,
                         AnalyticsClientPort analyticsClient) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.analyticsClient = analyticsClient;
    }

    @Override
    public Pedido crearPedido(Long usuarioId, List<Map<String, Object>> items) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        double total = 0.0;
        List<DetallePedido> detalles = new ArrayList<>();

        for (Map<String, Object> item : items) {
            Number productoIdNum = (Number) item.get("productoId");
            Number cantidadNum = (Number) item.get("cantidad");

            if (productoIdNum == null || cantidadNum == null) {
                throw new IllegalArgumentException("Cada item debe contener productoId y cantidad");
            }

            Long productoId = productoIdNum.longValue();
            int cantidad = cantidadNum.intValue();

            Producto producto = productoRepository.buscarPorId(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto con ID " + productoId + " no encontrado"));

            if (!Boolean.TRUE.equals(producto.getDisponibilidad())) {
                throw new RuntimeException("El producto " + producto.getNombre() + " no esta disponible en este momento");
            }

            DetallePedido detalle = DetallePedido.builder()
                    .producto(producto)
                    .cantidad(cantidad)
                    .build();

            detalles.add(detalle);
            total += producto.getPrecio() * cantidad;
        }

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .detalles(detalles)
                .total(total)
                .estado(EstadoPedido.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .build();

        return pedidoRepository.guardar(pedido);
    }

    @Override
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    @Override
    public List<Pedido> obtenerTodos() {
        return pedidoRepository.buscarTodos();
    }

    @Override
    public List<Pedido> obtenerPorUsuario(Long usuarioId) {
        return pedidoRepository.buscarPorUsuarioId(usuarioId);
    }

    @Override
    public Pedido actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = obtenerPorId(id);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.guardar(pedido);
    }

    @Override
    public Map<String, Object> obtenerReporteAnalitico() {
        try {
            return analyticsClient.obtenerReporteVentas();
        } catch (Exception e) {
            // Si el servicio de Python no esta levantado o falla, devolvemos un fallback informativo
            return Map.of(
                "error", "No se pudo establecer comunicacion con el microservicio de analitica de Python",
                "detalles", e.getMessage()
            );
        }
    }
}
