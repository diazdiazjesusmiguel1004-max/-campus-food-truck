-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS foodtruck;
USE foodtruck;

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Productos
CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL,
    disponibilidad BOOLEAN NOT NULL,
    imagen_url VARCHAR(500) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Pedidos
CREATE TABLE IF NOT EXISTS pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    total DOUBLE NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla Detalle de Pedidos
CREATE TABLE IF NOT EXISTS detalle_pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar Datos Semilla
-- Password encriptada con BCrypt para '123456'
-- usuarios (rol: ESTUDIANTE, COCINERO)
INSERT INTO usuarios (id, nombre, email, password, rol) VALUES
(1, 'Juan Perez', 'estudiante@campus.edu', '$2a$10$tZ277.62jWshf49qF/0jD.yH73w3YwzE2t1x51897XbT.w4yK6D1W', 'ESTUDIANTE'),
(2, 'Chef Carlos', 'cocinero@campus.edu', '$2a$10$tZ277.62jWshf49qF/0jD.yH73w3YwzE2t1x51897XbT.w4yK6D1W', 'COCINERO')
ON DUPLICATE KEY UPDATE id=id;

-- productos
INSERT INTO productos (id, nombre, precio, disponibilidad, imagen_url) VALUES
(1, 'Hamburguesa Clasica', 4.50, 1, 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500&auto=format&fit=crop&q=60'),
(2, 'Papas Fritas Medias', 2.00, 1, 'https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=500&auto=format&fit=crop&q=60'),
(3, 'Refresco de Cola', 1.50, 1, 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=500&auto=format&fit=crop&q=60'),
(4, 'Cafe Expreso', 1.20, 1, 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=500&auto=format&fit=crop&q=60'),
(5, 'Sandwich de Pollo', 3.80, 1, 'https://images.unsplash.com/photo-1521390188846-e2a3a97453a0?w=500&auto=format&fit=crop&q=60'),
(6, 'Empanada de Carne', 1.00, 1, 'https://images.unsplash.com/photo-1556910103-1c02745aae4d?w=500&auto=format&fit=crop&q=60'),
(7, 'Pizza Margarita', 5.50, 1, 'https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?w=500&auto=format&fit=crop&q=60'),
(8, 'Donas Glaseadas', 1.80, 1, 'https://images.unsplash.com/photo-1551024601-bec78aea704b?w=500&auto=format&fit=crop&q=60'),
(9, 'Torta de Chocolate', 3.20, 1, 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=500&auto=format&fit=crop&q=60'),
(10, 'Tacos al Pastor', 4.80, 1, 'https://images.unsplash.com/photo-1551504734-5ee1c4a1479b?w=500&auto=format&fit=crop&q=60'),
(11, 'Burrito de Res', 5.00, 1, 'https://images.unsplash.com/photo-1562059390-a761a084768e?w=500&auto=format&fit=crop&q=60'),
(12, 'Hot Dog Clasico', 2.50, 1, 'https://images.unsplash.com/photo-1619740455993-9e612b1af08a?w=500&auto=format&fit=crop&q=60'),
(13, 'Waffles con Miel', 3.00, 1, 'https://images.unsplash.com/photo-1504754524776-8f4f37790ca0?w=500&auto=format&fit=crop&q=60'),
(14, 'Jugos Naturales', 2.20, 1, 'https://images.unsplash.com/photo-1536882240095-0379873feb4e?w=500&auto=format&fit=crop&q=60'),
(15, 'Ensalada Caesar', 4.20, 1, 'https://images.unsplash.com/photo-1550304943-4f24f54ddde9?w=500&auto=format&fit=crop&q=60'),
(16, 'Croissant Mixto', 2.80, 1, 'https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=500&auto=format&fit=crop&q=60'),
(17, 'Crepa de Nutella', 3.50, 1, 'https://images.unsplash.com/photo-1519676867240-f03562e64548?w=500&auto=format&fit=crop&q=60'),
(18, 'Lasana de Carne', 6.00, 1, 'https://images.unsplash.com/photo-1574894709920-11b28e7367e3?w=500&auto=format&fit=crop&q=60'),
(19, 'Malteada de Fresa', 3.00, 1, 'https://images.unsplash.com/photo-1579954115545-a95591f28bfc?w=500&auto=format&fit=crop&q=60'),
(20, 'Te Helado de Limon', 1.80, 1, 'https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=500&auto=format&fit=crop&q=60'),
(21, 'Muffin de Arandanos', 1.60, 1, 'https://images.unsplash.com/photo-1607958996333-41aef7caefaa?w=500&auto=format&fit=crop&q=60'),
(22, 'Brownie con Helado', 3.50, 1, 'https://images.unsplash.com/photo-1564355808539-22fda35bed7e?w=500&auto=format&fit=crop&q=60'),
(23, 'Club Sandwich', 4.90, 1, 'https://images.unsplash.com/photo-1567234669003-dce7a7a88821?w=500&auto=format&fit=crop&q=60'),
(24, 'Papas Rusticas', 2.50, 1, 'https://images.unsplash.com/photo-1608039829572-78524f79c4c7?w=500&auto=format&fit=crop&q=60'),
(25, 'Nuggets de Pollo', 3.20, 1, 'https://images.unsplash.com/photo-1569058242253-92a9c755a0ec?w=500&auto=format&fit=crop&q=60'),
(26, 'Nachos con Queso', 3.50, 1, 'https://images.unsplash.com/photo-1513456852971-30c0b8199d4d?w=500&auto=format&fit=crop&q=60'),
(27, 'Aros de Cebolla', 2.80, 1, 'https://images.unsplash.com/photo-1555126634-323283e090fa?w=500&auto=format&fit=crop&q=60'),
(28, 'Choripan Clasico', 3.50, 1, 'https://images.unsplash.com/photo-1514933651103-005eec06c04b?w=500&auto=format&fit=crop&q=60'),
(29, 'Limonada con Menta', 2.00, 1, 'https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=500&auto=format&fit=crop&q=60'),
(30, 'Tequenos de Queso', 3.00, 1, 'https://images.unsplash.com/photo-1541532713592-79a0317b6b77?w=500&auto=format&fit=crop&q=60'),
(31, 'Papas Cheddar y Bacon', 3.50, 1, 'https://images.unsplash.com/photo-1576107232684-1279f390859f?w=500&auto=format&fit=crop&q=60'),
(32, 'Tarta de Fresa', 2.50, 1, 'https://images.unsplash.com/photo-1519869325930-281384150729?w=500&auto=format&fit=crop&q=60'),
(33, 'Pan de Ajo Crujiente', 2.00, 1, 'https://images.unsplash.com/photo-1573140247632-f8fd74997d5c?w=500&auto=format&fit=crop&q=60'),
(34, 'Ensalada de Frutas', 3.00, 1, 'https://images.unsplash.com/photo-1615485290382-441e4d049cb5?w=500&auto=format&fit=crop&q=60'),
(35, 'Milanesa de Pollo', 5.50, 1, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=500&auto=format&fit=crop&q=60'),
(36, 'Brownie con Nueces', 2.20, 1, 'https://images.unsplash.com/photo-1606313564200-e75d5e30476c?w=500&auto=format&fit=crop&q=60'),
(37, 'Te Verde Caliente', 1.50, 1, 'https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=500&auto=format&fit=crop&q=60'),
(38, 'Chicha Morada Helada', 1.80, 1, 'https://images.unsplash.com/photo-1497534446932-c925b458314e?w=500&auto=format&fit=crop&q=60'),
(39, 'Empanada de Queso', 1.20, 1, 'https://images.unsplash.com/photo-1600891964599-f61ba0e24092?w=500&auto=format&fit=crop&q=60')
ON DUPLICATE KEY UPDATE id=id;

-- Seed orders for analytics simulation (different dates/hours of yesterday and today)
-- Pedido 1: Cafe y sandwich en la mañana de ayer (8:30 AM)
INSERT INTO pedidos (id, usuario_id, total, estado, fecha_creacion) VALUES
(1, 1, 6.20, 'LISTO', DATE_SUB(NOW(), INTERVAL '1 11:30' DAY_HOUR))
ON DUPLICATE KEY UPDATE id=id;
INSERT INTO detalle_pedidos (id, pedido_id, producto_id, cantidad) VALUES
(1, 1, 4, 2),
(2, 1, 5, 1)
ON DUPLICATE KEY UPDATE id=id;

-- Pedido 2: Cafe y sandwich en la mañana de ayer (9:15 AM)
INSERT INTO pedidos (id, usuario_id, total, estado, fecha_creacion) VALUES
(2, 1, 5.00, 'LISTO', DATE_SUB(NOW(), INTERVAL '1 10:45' DAY_HOUR))
ON DUPLICATE KEY UPDATE id=id;
INSERT INTO detalle_pedidos (id, pedido_id, producto_id, cantidad) VALUES
(3, 2, 4, 1),
(4, 2, 5, 1)
ON DUPLICATE KEY UPDATE id=id;

-- Pedido 3: Hamburguesa, papas y gaseosa en el almuerzo de ayer (12:10 PM)
INSERT INTO pedidos (id, usuario_id, total, estado, fecha_creacion) VALUES
(3, 1, 8.00, 'LISTO', DATE_SUB(NOW(), INTERVAL '1 7:50' DAY_HOUR))
ON DUPLICATE KEY UPDATE id=id;
INSERT INTO detalle_pedidos (id, pedido_id, producto_id, cantidad) VALUES
(5, 3, 1, 1),
(6, 3, 2, 1),
(7, 3, 3, 1)
ON DUPLICATE KEY UPDATE id=id;

-- Pedido 4: Dos Hamburguesas y refrescos en el almuerzo de ayer (1:05 PM)
INSERT INTO pedidos (id, usuario_id, total, estado, fecha_creacion) VALUES
(4, 1, 12.00, 'LISTO', DATE_SUB(NOW(), INTERVAL '1 6:55' DAY_HOUR))
ON DUPLICATE KEY UPDATE id=id;
INSERT INTO detalle_pedidos (id, pedido_id, producto_id, cantidad) VALUES
(8, 4, 1, 2),
(9, 4, 3, 2)
ON DUPLICATE KEY UPDATE id=id;

-- Pedido 5: Hamburguesa y papas por la noche de ayer (6:20 PM)
INSERT INTO pedidos (id, usuario_id, total, estado, fecha_creacion) VALUES
(5, 1, 8.50, 'LISTO', DATE_SUB(NOW(), INTERVAL '1 1:40' DAY_HOUR))
ON DUPLICATE KEY UPDATE id=id;
INSERT INTO detalle_pedidos (id, pedido_id, producto_id, cantidad) VALUES
(10, 5, 1, 1),
(11, 5, 2, 2)
ON DUPLICATE KEY UPDATE id=id;

-- Pedido 6: Cafe y sandwich en la mañana de hoy (8:45 AM)
INSERT INTO pedidos (id, usuario_id, total, estado, fecha_creacion) VALUES
(6, 1, 5.00, 'LISTO', CONCAT(CURDATE(), ' 08:45:00'))
ON DUPLICATE KEY UPDATE id=id;
INSERT INTO detalle_pedidos (id, pedido_id, producto_id, cantidad) VALUES
(12, 6, 4, 1),
(13, 6, 5, 1)
ON DUPLICATE KEY UPDATE id=id;

-- Pedido 7: Hamburguesa, papas y gaseosa al almuerzo de hoy (12:30 PM)
INSERT INTO pedidos (id, usuario_id, total, estado, fecha_creacion) VALUES
(7, 1, 8.00, 'PREPARANDO', CONCAT(CURDATE(), ' 12:30:00'))
ON DUPLICATE KEY UPDATE id=id;
INSERT INTO detalle_pedidos (id, pedido_id, producto_id, cantidad) VALUES
(14, 7, 1, 1),
(15, 7, 2, 1),
(16, 7, 3, 1)
ON DUPLICATE KEY UPDATE id=id;

-- Pedido 8: Sandwich y refresco al almuerzo de hoy (12:55 PM)
INSERT INTO pedidos (id, usuario_id, total, estado, fecha_creacion) VALUES
(8, 1, 5.30, 'PENDIENTE', CONCAT(CURDATE(), ' 12:55:00'))
ON DUPLICATE KEY UPDATE id=id;
INSERT INTO detalle_pedidos (id, pedido_id, producto_id, cantidad) VALUES
(17, 8, 5, 1),
(18, 8, 3, 1)
ON DUPLICATE KEY UPDATE id=id;
