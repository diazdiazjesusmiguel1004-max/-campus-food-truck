package com.campus.foodtruck.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {
    private Long id;
    private Producto producto;
    private Integer cantidad;
}
