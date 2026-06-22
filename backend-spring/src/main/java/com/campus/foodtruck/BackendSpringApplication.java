package com.campus.foodtruck;

import com.campus.foodtruck.domain.model.EstadoPedido;
import com.campus.foodtruck.domain.model.Rol;
import com.campus.foodtruck.domain.model.Usuario;
import com.campus.foodtruck.domain.ports.in.PedidoUseCase;
import com.campus.foodtruck.domain.ports.in.ProductoUseCase;
import com.campus.foodtruck.domain.ports.in.UsuarioUseCase;
import com.campus.foodtruck.infrastructure.adapters.out.database.entity.DetallePedidoEntity;
import com.campus.foodtruck.infrastructure.adapters.out.database.entity.PedidoEntity;
import com.campus.foodtruck.infrastructure.adapters.out.database.entity.ProductoEntity;
import com.campus.foodtruck.infrastructure.adapters.out.database.entity.UsuarioEntity;
import com.campus.foodtruck.infrastructure.adapters.out.database.repository.SpringDataPedidoRepository;
import com.campus.foodtruck.infrastructure.adapters.out.database.repository.SpringDataProductoRepository;
import com.campus.foodtruck.infrastructure.adapters.out.database.repository.SpringDataUsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class BackendSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendSpringApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(
            SpringDataUsuarioRepository usuarioRepository,
            SpringDataProductoRepository productoRepository,
            SpringDataPedidoRepository pedidoRepository,
            PasswordEncoder encoder) {
        return args -> {
            // Seed Usuarios if empty
            if (usuarioRepository.count() == 0) {
                UsuarioEntity estudiante = UsuarioEntity.builder()
                        .nombre("Juan Perez")
                        .email("estudiante@campus.edu")
                        .password(encoder.encode("123456"))
                        .rol(Rol.ESTUDIANTE)
                        .build();

                UsuarioEntity cocinero = UsuarioEntity.builder()
                        .nombre("Chef Carlos")
                        .email("cocinero@campus.edu")
                        .password(encoder.encode("123456"))
                        .rol(Rol.COCINERO)
                        .build();

                usuarioRepository.saveAll(List.of(estudiante, cocinero));
                System.out.println(">>> Usuarios semilla creados (estudiante@campus.edu / cocinero@campus.edu)");
            }

            // Seed Productos if empty
            if (productoRepository.count() == 0) {
                ProductoEntity p1 = ProductoEntity.builder()
                        .nombre("Hamburguesa Clasica")
                        .precio(4.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p2 = ProductoEntity.builder()
                        .nombre("Papas Fritas Medias")
                        .precio(2.00)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p3 = ProductoEntity.builder()
                        .nombre("Refresco de Cola")
                        .precio(1.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p4 = ProductoEntity.builder()
                        .nombre("Cafe Expreso")
                        .precio(1.20)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p5 = ProductoEntity.builder()
                        .nombre("Sandwich de Pollo")
                        .precio(3.80)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1521390188846-e2a3a97453a0?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p6 = ProductoEntity.builder()
                        .nombre("Empanada de Carne")
                        .precio(1.00)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1556910103-1c02745aae4d?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p7 = ProductoEntity.builder()
                        .nombre("Pizza Margarita")
                        .precio(5.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p8 = ProductoEntity.builder()
                        .nombre("Donas Glaseadas")
                        .precio(1.80)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1551024601-bec78aea704b?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p9 = ProductoEntity.builder()
                        .nombre("Torta de Chocolate")
                        .precio(3.20)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p10 = ProductoEntity.builder()
                        .nombre("Tacos al Pastor")
                        .precio(4.80)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1551504734-5ee1c4a1479b?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p11 = ProductoEntity.builder()
                        .nombre("Burrito de Res")
                        .precio(5.00)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1562059390-a761a084768e?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p12 = ProductoEntity.builder()
                        .nombre("Hot Dog Clasico")
                        .precio(2.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1619740455993-9e612b1af08a?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p13 = ProductoEntity.builder()
                        .nombre("Waffles con Miel")
                        .precio(3.00)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1504754524776-8f4f37790ca0?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p14 = ProductoEntity.builder()
                        .nombre("Jugos Naturales")
                        .precio(2.20)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1536882240095-0379873feb4e?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p15 = ProductoEntity.builder()
                        .nombre("Ensalada Caesar")
                        .precio(4.20)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1550304943-4f24f54ddde9?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p16 = ProductoEntity.builder()
                        .nombre("Croissant Mixto")
                        .precio(2.80)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p17 = ProductoEntity.builder()
                        .nombre("Crepa de Nutella")
                        .precio(3.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1519676867240-f03562e64548?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p18 = ProductoEntity.builder()
                        .nombre("Lasana de Carne")
                        .precio(6.00)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1574894709920-11b28e7367e3?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p19 = ProductoEntity.builder()
                        .nombre("Malteada de Fresa")
                        .precio(3.00)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1579954115545-a95591f28bfc?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p20 = ProductoEntity.builder()
                        .nombre("Te Helado de Limon")
                        .precio(1.80)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p21 = ProductoEntity.builder()
                        .nombre("Muffin de Arandanos")
                        .precio(1.60)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1607958996333-41aef7caefaa?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p22 = ProductoEntity.builder()
                        .nombre("Brownie con Helado")
                        .precio(3.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1564355808539-22fda35bed7e?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p23 = ProductoEntity.builder()
                        .nombre("Club Sandwich")
                        .precio(4.90)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1567234669003-dce7a7a88821?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p24 = ProductoEntity.builder()
                        .nombre("Papas Rusticas")
                        .precio(2.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1608039829572-78524f79c4c7?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p25 = ProductoEntity.builder()
                        .nombre("Nuggets de Pollo")
                        .precio(3.20)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1569058242253-92a9c755a0ec?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p26 = ProductoEntity.builder()
                        .nombre("Nachos con Queso")
                        .precio(3.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1513456852971-30c0b8199d4d?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p27 = ProductoEntity.builder()
                        .nombre("Aros de Cebolla")
                        .precio(2.80)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1555126634-323283e090fa?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p28 = ProductoEntity.builder()
                        .nombre("Choripan Clasico")
                        .precio(3.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1514933651103-005eec06c04b?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p29 = ProductoEntity.builder()
                        .nombre("Limonada con Menta")
                        .precio(2.00)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p30 = ProductoEntity.builder()
                        .nombre("Tequenos de Queso")
                        .precio(3.00)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1541532713592-79a0317b6b77?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p31 = ProductoEntity.builder()
                        .nombre("Papas Cheddar y Bacon")
                        .precio(3.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1576107232684-1279f390859f?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p32 = ProductoEntity.builder()
                        .nombre("Tarta de Fresa")
                        .precio(2.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1519869325930-281384150729?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p33 = ProductoEntity.builder()
                        .nombre("Pan de Ajo Crujiente")
                        .precio(2.00)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1573140247632-f8fd74997d5c?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p34 = ProductoEntity.builder()
                        .nombre("Ensalada de Frutas")
                        .precio(3.00)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1615485290382-441e4d049cb5?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p35 = ProductoEntity.builder()
                        .nombre("Milanesa de Pollo")
                        .precio(5.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p36 = ProductoEntity.builder()
                        .nombre("Brownie con Nueces")
                        .precio(2.20)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1606313564200-e75d5e30476c?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p37 = ProductoEntity.builder()
                        .nombre("Te Verde Caliente")
                        .precio(1.50)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p38 = ProductoEntity.builder()
                        .nombre("Chicha Morada Helada")
                        .precio(1.80)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1497534446932-c925b458314e?w=500&auto=format&fit=crop&q=60")
                        .build();

                ProductoEntity p39 = ProductoEntity.builder()
                        .nombre("Empanada de Queso")
                        .precio(1.20)
                        .disponibilidad(true)
                        .imagenUrl("https://images.unsplash.com/photo-1600891964599-f61ba0e24092?w=500&auto=format&fit=crop&q=60")
                        .build();

                productoRepository.saveAll(List.of(
                    p1, p2, p3, p4, p5, p6, p7, p8, p9, p10,
                    p11, p12, p13, p14, p15, p16, p17, p18, p19, p20,
                    p21, p22, p23, p24, p25, p26, p27, p28, p29, p30,
                    p31, p32, p33, p34, p35, p36, p37, p38, p39
                ));
                System.out.println(">>> Productos semilla creados");
            }

            // Seed Pedidos if empty (para simular analiticas)
            if (pedidoRepository.count() == 0) {
                UsuarioEntity juan = usuarioRepository.findByEmail("estudiante@campus.edu").orElse(null);
                List<ProductoEntity> productos = productoRepository.findAll();

                if (juan != null && !productos.isEmpty()) {
                    ProductoEntity burger = productos.get(0); // Hamburguesa
                    ProductoEntity papas = productos.get(1);  // Papas Fritas
                    ProductoEntity refresco = productos.get(2); // Refresco
                    ProductoEntity cafe = productos.get(3); // Cafe
                    ProductoEntity sandwich = productos.get(4); // Sandwich

                    // Pedidos en el desayuno (8:00 AM - 10:00 AM)
                    crearPedidoSemilla(pedidoRepository, juan, List.of(cafe, cafe), List.of(2, 1), EstadoPedido.LISTO, LocalDateTime.now().minusDays(1).withHour(8).withMinute(30));
                    crearPedidoSemilla(pedidoRepository, juan, List.of(cafe, sandwich), List.of(1, 1), EstadoPedido.LISTO, LocalDateTime.now().minusDays(1).withHour(9).withMinute(15));
                    
                    // Pedidos al almuerzo (12:00 PM - 2:00 PM) - ¡Hora Pico!
                    crearPedidoSemilla(pedidoRepository, juan, List.of(burger, papas, refresco), List.of(1, 1, 1), EstadoPedido.LISTO, LocalDateTime.now().minusDays(1).withHour(12).withMinute(10));
                    crearPedidoSemilla(pedidoRepository, juan, List.of(burger, refresco), List.of(2, 2), EstadoPedido.LISTO, LocalDateTime.now().minusDays(1).withHour(13).withMinute(05));
                    crearPedidoSemilla(pedidoRepository, juan, List.of(sandwich, papas), List.of(1, 1), EstadoPedido.LISTO, LocalDateTime.now().minusDays(1).withHour(13).withMinute(45));
                    
                    // Pedidos por la tarde/noche (5:00 PM - 7:00 PM)
                    crearPedidoSemilla(pedidoRepository, juan, List.of(burger, papas), List.of(1, 2), EstadoPedido.LISTO, LocalDateTime.now().minusDays(1).withHour(18).withMinute(20));
                    
                    // Pedidos de hoy
                    crearPedidoSemilla(pedidoRepository, juan, List.of(cafe, sandwich), List.of(1, 1), EstadoPedido.LISTO, LocalDateTime.now().withHour(8).withMinute(45));
                    crearPedidoSemilla(pedidoRepository, juan, List.of(burger, papas, refresco), List.of(1, 1, 1), EstadoPedido.PREPARANDO, LocalDateTime.now().withHour(12).withMinute(30));
                    crearPedidoSemilla(pedidoRepository, juan, List.of(sandwich, refresco), List.of(1, 1), EstadoPedido.PENDIENTE, LocalDateTime.now().withHour(12).withMinute(55));

                    System.out.println(">>> Pedidos semilla e historial creados con exito para simulacion de analiticas");
                }
            }
        };
    }

    private void crearPedidoSemilla(
            SpringDataPedidoRepository repository,
            UsuarioEntity usuario,
            List<ProductoEntity> productos,
            List<Integer> cantidades,
            EstadoPedido estado,
            LocalDateTime fecha) {
        
        double total = 0;
        for (int i = 0; i < productos.size(); i++) {
            total += productos.get(i).getPrecio() * cantidades.get(i);
        }

        PedidoEntity pedido = PedidoEntity.builder()
                .usuario(usuario)
                .total(total)
                .estado(estado)
                .fechaCreacion(fecha)
                .build();

        List<DetallePedidoEntity> detalles = new java.util.ArrayList<>();
        for (int i = 0; i < productos.size(); i++) {
            detalles.add(DetallePedidoEntity.builder()
                    .pedido(pedido)
                    .producto(productos.get(i))
                    .cantidad(cantidades.get(i))
                    .build());
        }
        pedido.setDetalles(detalles);

        repository.save(pedido);
    }
}
