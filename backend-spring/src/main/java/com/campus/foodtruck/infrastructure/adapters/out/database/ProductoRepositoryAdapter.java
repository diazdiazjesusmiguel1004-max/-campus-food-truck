package com.campus.foodtruck.infrastructure.adapters.out.database;

import com.campus.foodtruck.domain.model.Producto;
import com.campus.foodtruck.domain.ports.out.ProductoRepositoryPort;
import com.campus.foodtruck.infrastructure.adapters.out.database.entity.ProductoEntity;
import com.campus.foodtruck.infrastructure.adapters.out.database.repository.SpringDataProductoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductoRepositoryAdapter implements ProductoRepositoryPort {

    private final SpringDataProductoRepository repository;

    public ProductoRepositoryAdapter(SpringDataProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Producto guardar(Producto producto) {
        ProductoEntity entity = EntityMapper.toEntity(producto);
        ProductoEntity savedEntity = repository.save(entity);
        return EntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return repository.findById(id).map(EntityMapper::toDomain);
    }

    @Override
    public List<Producto> buscarTodos() {
        return repository.findAll().stream()
                .map(EntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
