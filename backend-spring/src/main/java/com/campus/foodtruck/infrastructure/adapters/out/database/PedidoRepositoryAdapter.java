package com.campus.foodtruck.infrastructure.adapters.out.database;

import com.campus.foodtruck.domain.model.Pedido;
import com.campus.foodtruck.domain.ports.out.PedidoRepositoryPort;
import com.campus.foodtruck.infrastructure.adapters.out.database.entity.PedidoEntity;
import com.campus.foodtruck.infrastructure.adapters.out.database.repository.SpringDataPedidoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final SpringDataPedidoRepository repository;

    public PedidoRepositoryAdapter(SpringDataPedidoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoEntity entity = EntityMapper.toEntity(pedido);
        PedidoEntity savedEntity = repository.save(entity);
        return EntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return repository.findById(id).map(EntityMapper::toDomain);
    }

    @Override
    public List<Pedido> buscarTodos() {
        return repository.findAllByOrderByFechaCreacionDesc().stream()
                .map(EntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> buscarPorUsuarioId(Long usuarioId) {
        return repository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId).stream()
                .map(EntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
