package com.campus.foodtruck.infrastructure.config;

import com.campus.foodtruck.application.service.PedidoService;
import com.campus.foodtruck.application.service.ProductoService;
import com.campus.foodtruck.application.service.UsuarioService;
import com.campus.foodtruck.domain.ports.in.PedidoUseCase;
import com.campus.foodtruck.domain.ports.in.ProductoUseCase;
import com.campus.foodtruck.domain.ports.in.UsuarioUseCase;
import com.campus.foodtruck.domain.ports.out.AnalyticsClientPort;
import com.campus.foodtruck.domain.ports.out.PasswordEncoderPort;
import com.campus.foodtruck.domain.ports.out.PedidoRepositoryPort;
import com.campus.foodtruck.domain.ports.out.ProductoRepositoryPort;
import com.campus.foodtruck.domain.ports.out.TokenServicePort;
import com.campus.foodtruck.domain.ports.out.UsuarioRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioRepositoryPort usuarioRepository,
                                         PasswordEncoderPort passwordEncoder,
                                         TokenServicePort tokenService) {
        return new UsuarioService(usuarioRepository, passwordEncoder, tokenService);
    }

    @Bean
    public ProductoUseCase productoUseCase(ProductoRepositoryPort productoRepository) {
        return new ProductoService(productoRepository);
    }

    @Bean
    public PedidoUseCase pedidoUseCase(PedidoRepositoryPort pedidoRepository,
                                       UsuarioRepositoryPort usuarioRepository,
                                       ProductoRepositoryPort productoRepository,
                                       AnalyticsClientPort analyticsClient) {
        return new PedidoService(pedidoRepository, usuarioRepository, productoRepository, analyticsClient);
    }
}
