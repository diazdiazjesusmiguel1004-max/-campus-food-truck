package com.campus.foodtruck.application.service;

import com.campus.foodtruck.domain.model.Producto;
import com.campus.foodtruck.domain.ports.in.ProductoUseCase;
import com.campus.foodtruck.domain.ports.out.ProductoRepositoryPort;

import java.util.List;

public class ProductoService implements ProductoUseCase {

    private final ProductoRepositoryPort productoRepository;

    public ProductoService(ProductoRepositoryPort productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> obtenerTodos() {
        return productoRepository.buscarTodos();
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return productoRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Override
    public Producto guardar(Producto producto) {
        return productoRepository.guardar(producto);
    }

    @Override
    public void eliminar(Long id) {
        productoRepository.eliminar(id);
    }
}
