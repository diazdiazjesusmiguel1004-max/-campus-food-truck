package com.campus.foodtruck.infrastructure.adapters.out.database.repository;

import com.campus.foodtruck.infrastructure.adapters.out.database.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataPedidoRepository extends JpaRepository<PedidoEntity, Long> {
    List<PedidoEntity> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);
    List<PedidoEntity> findAllByOrderByFechaCreacionDesc();
}
