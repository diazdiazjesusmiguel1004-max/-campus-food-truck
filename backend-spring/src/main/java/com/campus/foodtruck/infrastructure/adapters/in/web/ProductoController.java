package com.campus.foodtruck.infrastructure.adapters.in.web;

import com.campus.foodtruck.domain.model.Producto;
import com.campus.foodtruck.domain.ports.in.ProductoUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoUseCase productoUseCase;

    public ProductoController(ProductoUseCase productoUseCase) {
        this.productoUseCase = productoUseCase;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoUseCase.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productoUseCase.obtenerPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody ProductoDto dto) {
        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .disponibilidad(dto.getDisponibilidad())
                .imagenUrl(dto.getImagenUrl())
                .build();
        Producto guardado = productoUseCase.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoDto dto) {
        try {
            Producto productoExistente = productoUseCase.obtenerPorId(id);
            productoExistente.setNombre(dto.getNombre());
            productoExistente.setPrecio(dto.getPrecio());
            productoExistente.setDisponibilidad(dto.getDisponibilidad());
            productoExistente.setImagenUrl(dto.getImagenUrl());
            
            Producto guardado = productoUseCase.guardar(productoExistente);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            productoUseCase.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Data
    public static class ProductoDto {
        @NotBlank(message = "El nombre es obligatorio")
        private String nombre;

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser positivo")
        private Double precio;

        @NotNull(message = "La disponibilidad es obligatoria")
        private Boolean disponibilidad;

        private String imagenUrl;
    }
}
