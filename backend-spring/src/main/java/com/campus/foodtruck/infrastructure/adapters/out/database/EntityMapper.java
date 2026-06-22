package com.campus.foodtruck.infrastructure.adapters.out.database;

import com.campus.foodtruck.domain.model.DetallePedido;
import com.campus.foodtruck.domain.model.Pedido;
import com.campus.foodtruck.domain.model.Producto;
import com.campus.foodtruck.domain.model.Usuario;
import com.campus.foodtruck.infrastructure.adapters.out.database.entity.DetallePedidoEntity;
import com.campus.foodtruck.infrastructure.adapters.out.database.entity.PedidoEntity;
import com.campus.foodtruck.infrastructure.adapters.out.database.entity.ProductoEntity;
import com.campus.foodtruck.infrastructure.adapters.out.database.entity.UsuarioEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityMapper {

    public static Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;
        return Usuario.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .rol(entity.getRol())
                .build();
    }

    public static UsuarioEntity toEntity(Usuario domain) {
        if (domain == null) return null;
        return UsuarioEntity.builder()
                .id(domain.getId())
                .nombre(domain.getNombre())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .rol(domain.getRol())
                .build();
    }

    public static Producto toDomain(ProductoEntity entity) {
        if (entity == null) return null;
        return Producto.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .precio(entity.getPrecio())
                .disponibilidad(entity.getDisponibilidad())
                .imagenUrl(entity.getImagenUrl())
                .build();
    }

    public static ProductoEntity toEntity(Producto domain) {
        if (domain == null) return null;
        return ProductoEntity.builder()
                .id(domain.getId())
                .nombre(domain.getNombre())
                .precio(domain.getPrecio())
                .disponibilidad(domain.getDisponibilidad())
                .imagenUrl(domain.getImagenUrl())
                .build();
    }

    public static Pedido toDomain(PedidoEntity entity) {
        if (entity == null) return null;
        List<DetallePedido> detalles = entity.getDetalles() == null ? new ArrayList<>() :
                entity.getDetalles().stream()
                        .map(EntityMapper::toDomain)
                        .collect(Collectors.toList());

        return Pedido.builder()
                .id(entity.getId())
                .usuario(toDomain(entity.getUsuario()))
                .total(entity.getTotal())
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .detalles(detalles)
                .build();
    }

    public static PedidoEntity toEntity(Pedido domain) {
        if (domain == null) return null;
        PedidoEntity entity = PedidoEntity.builder()
                .id(domain.getId())
                .usuario(toEntity(domain.getUsuario()))
                .total(domain.getTotal())
                .estado(domain.getEstado())
                .fechaCreacion(domain.getFechaCreacion())
                .build();

        if (domain.getDetalles() != null) {
            List<DetallePedidoEntity> detallesEntities = domain.getDetalles().stream()
                    .map(d -> {
                        DetallePedidoEntity de = toEntity(d);
                        de.setPedido(entity); // Set bidirectional relationship
                        return de;
                    })
                    .collect(Collectors.toList());
            entity.setDetalles(detallesEntities);
        }
        return entity;
    }

    public static DetallePedido toDomain(DetallePedidoEntity entity) {
        if (entity == null) return null;
        return DetallePedido.builder()
                .id(entity.getId())
                .producto(toDomain(entity.getProducto()))
                .cantidad(entity.getCantidad())
                .build();
    }

    public static DetallePedidoEntity toEntity(DetallePedido domain) {
        if (domain == null) return null;
        return DetallePedidoEntity.builder()
                .id(domain.getId())
                .producto(toEntity(domain.getProducto()))
                .cantidad(domain.getCantidad())
                .build();
    }
}
