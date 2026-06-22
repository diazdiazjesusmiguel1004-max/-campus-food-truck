package com.campus.foodtruck.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    private Long id;
    private Usuario usuario;
    private Double total;
    private EstadoPedido estado;
    private LocalDateTime fechaCreacion;
    private List<DetallePedido> detalles;
}
