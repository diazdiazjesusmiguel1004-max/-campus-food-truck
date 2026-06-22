package com.campus.foodtruck.infrastructure.adapters.in.web;

import com.campus.foodtruck.domain.model.EstadoPedido;
import com.campus.foodtruck.domain.model.Pedido;
import com.campus.foodtruck.domain.model.Usuario;
import com.campus.foodtruck.domain.ports.in.PedidoUseCase;
import com.campus.foodtruck.domain.ports.in.UsuarioUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoUseCase pedidoUseCase;
    private final UsuarioUseCase usuarioUseCase;

    public PedidoController(PedidoUseCase pedidoUseCase, UsuarioUseCase usuarioUseCase) {
        this.pedidoUseCase = pedidoUseCase;
        this.usuarioUseCase = usuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody CrearPedidoDto dto, Principal principal) {
        try {
            String email = principal.getName();
            Usuario usuario = usuarioUseCase.obtenerPorEmail(email);
            
            // Convertimos DTO a lista de mapas para el puerto de entrada
            List<Map<String, Object>> items = new ArrayList<>();
            for (CrearPedidoDto.ItemPedidoDto itemDto : dto.getItems()) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("productoId", itemDto.getProductoId());
                itemMap.put("cantidad", itemDto.getCantidad());
                items.add(itemMap);
            }

            Pedido creado = pedidoUseCase.crearPedido(usuario.getId(), items);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        return ResponseEntity.ok(pedidoUseCase.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pedidoUseCase.obtenerPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/me")
    public ResponseEntity<List<Pedido>> obtenerMisPedidos(Principal principal) {
        String email = principal.getName();
        Usuario usuario = usuarioUseCase.obtenerPorEmail(email);
        return ResponseEntity.ok(pedidoUseCase.obtenerPorUsuario(usuario.getId()));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoUseCase.obtenerPorUsuario(usuarioId));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado) {
        try {
            Pedido actualizado = pedidoUseCase.actualizarEstado(id, estado);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> obtenerReporteAnalitico() {
        return ResponseEntity.ok(pedidoUseCase.obtenerReporteAnalitico());
    }

    // Endpoint publico/raw para que el microservicio Python consuma los datos directamente.
    // Devolvemos una estructura simplificada de pedidos para facilitar el procesamiento.
    @GetMapping("/raw-data")
    public ResponseEntity<List<Map<String, Object>>> obtenerDatosRaw() {
        List<Pedido> pedidos = pedidoUseCase.obtenerTodos();
        List<Map<String, Object>> rawList = new ArrayList<>();
        
        for (Pedido p : pedidos) {
            for (var detalle : p.getDetalles()) {
                Map<String, Object> record = new HashMap<>();
                record.put("pedido_id", p.getId());
                record.put("usuario_id", p.getUsuario().getId());
                record.put("nombre_usuario", p.getUsuario().getNombre());
                record.put("producto_id", detalle.getProducto().getId());
                record.put("nombre_producto", detalle.getProducto().getNombre());
                record.put("cantidad", detalle.getCantidad());
                record.put("precio_unitario", detalle.getProducto().getPrecio());
                record.put("total_pedido", p.getTotal());
                record.put("estado", p.getEstado().name());
                record.put("fecha_creacion", p.getFechaCreacion().toString());
                rawList.add(record);
            }
        }
        return ResponseEntity.ok(rawList);
    }

    @Data
    public static class CrearPedidoDto {
        @NotEmpty(message = "El pedido debe contener al menos un producto")
        private List<ItemPedidoDto> items;

        @Data
        public static class ItemPedidoDto {
            @NotNull(message = "El productoId es obligatorio")
            private Long productoId;

            @NotNull(message = "La cantidad es obligatoria")
            private Integer cantidad;
        }
    }
}
