package com.campus.foodtruck.infrastructure.adapters.out.database;

import com.campus.foodtruck.domain.model.Usuario;
import com.campus.foodtruck.domain.ports.out.UsuarioRepositoryPort;
import com.campus.foodtruck.infrastructure.adapters.out.database.entity.UsuarioEntity;
import com.campus.foodtruck.infrastructure.adapters.out.database.repository.SpringDataUsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final SpringDataUsuarioRepository repository;

    public UsuarioRepositoryAdapter(SpringDataUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = EntityMapper.toEntity(usuario);
        UsuarioEntity savedEntity = repository.save(entity);
        return EntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email).map(EntityMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id).map(EntityMapper::toDomain);
    }
}
