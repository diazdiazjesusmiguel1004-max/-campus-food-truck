# Campus Bites - Sistema de Gestión y Monitoreo de Pedidos para Food Truck del Campus

Este proyecto implementa una plataforma web distribuida para optimizar los pedidos en los puestos de comida o Food Trucks dentro del campus universitario. Permite a los estudiantes realizar pedidos para llevar evitando largas colas, a los cocineros gestionar comandas en tiempo real, y provee un módulo de analíticas en Python para optimizar la preparación de alimentos.

## 🚀 Arquitectura del Proyecto

El sistema utiliza una arquitectura de microservicios y sigue los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)** en el backend principal:

```
[ Frontend: HTML/CSS/JS (Glassmorphic Dashboard) ]
                     │ (HTTP REST + JWT)
                     ▼
┌────────────────────────────────────────────────────────┐
│             BACKEND-SPRING (Spring Boot 3)              │
│                                                        │
│   ┌──────────────────────────────────────────────┐     │
│   │             DOMAIN (CORE CENTRAL)            │     │
│   │                                              │     │
│   │   [Modelos]     [Puertos IN]  [Puertos OUT]  │     │
│   │  Usuario, Pedido   Casos de    Repositorios, │     │
│   │  Producto, etc.     Uso         Clientes     │     │
│   └──────────────────────────────────────────────┘     │
│         ▲                    ▲             │           │
│         │ (Implementa)       │ (Usa)       │ (Implementa)
│   ┌─────────────┐       ┌───────────┐ ┌──────────┐     │
│   │ APPLICATION │       │ WEB CONT. │ │ DATABASE │     │
│   │  Servicios  │       │ Adapters  │ │ Adapters │     │
│   └─────────────┘       └───────────┘ └──────────┘     │
└────────────────────────────────────────────────────────┘
          │ (Feign Client)
          ▼
┌────────────────────────────────────────────────────────┐
│             ANALYTICS-PYTHON (FastAPI)                 │
│                                                        │
│ Consume datos raw de Spring Boot -> Genera Analíticas   │
│ - Platos más vendidos   - Horas pico de pedidos        │
└────────────────────────────────────────────────────────┘
```

1. **Backend Principal (Spring Boot 3 & Java 17)**:
   - Implementa **Arquitectura Hexagonal** pura, desacoplando completamente el dominio de los frameworks (Spring Security, JPA, Feign, etc.).
   - Autenticación segura mediante **JWT (JSON Web Tokens)** con roles de acceso: `ROLE_ESTUDIANTE` y `ROLE_COCINERO`.
   - Cliente declarativo **OpenFeign** para interactuar de forma transparente con el microservicio de analíticas de Python.
   - Base de datos dual: **H2** (en memoria para desarrollo local instantáneo sin dependencias) y **MySQL** (para entornos de Docker y producción).

2. **Microservicio de Analítica (Python 3.13 & FastAPI)**:
   - Consume los datos del backend por medio de llamadas HTTP limpias.
   - Calcula métricas clave: horas con mayor volumen de pedidos (curva de demanda) y productos estrella.
   - Provee documentación auto-generada interactiva en `/docs` (Swagger UI).

3. **Frontend (Dashboard Moderno & Responsivo)**:
   - Interfaz con estética **Glassmorphism** premium basada en CSS Vanilla (sin frameworks pesados).
   - Panel de Estudiantes: Menú interactivo con iconos de comida dinámicos, carrito interactivo y rastreador de estado del pedido en tiempo real.
   - Panel de Cocineros: Tablero Kanban de cocina (Pendiente -> Preparando -> Listo) que se actualiza automáticamente, gestor de inventario/disponibilidad y gráficos dinámicos de analítica renderizados mediante **Chart.js**.

---

## 🛠️ Estructura del Repositorio

- **`backend-spring/`**: Código fuente de la API de Spring Boot.
  - `domain/model/`: Entidades de negocio puras.
  - `domain/ports/`: Interfaces de entrada y salida.
  - `application/service/`: Lógica de casos de uso (Servicios).
  - `infrastructure/adapters/`: Controladores REST, Seguridad JWT y persistencia JPA.
  - `src/main/resources/static/`: Archivos del frontend.
- **`analytics-python/`**: Código del microservicio de analítica.
- **`database/`**: Scripts SQL de inicialización.
- **`docker-compose.yml`**: Configuración para despliegue completo en un segundo.
- **`run-backend.ps1`** y **`run-analytics.ps1`**: Scripts automatizados de PowerShell para ejecución local instantánea en Windows.

---

## 🏃 Cómo Ejecutar el Proyecto

### Opción A: Ejecución Local Rápida (Recomendado para pruebas sin Docker)

Hemos creado scripts inteligentes en PowerShell que configuran y ejecutan todo de forma automática (incluso si no tienes Maven o dependencias instaladas):

1. **Iniciar el Backend (Spring Boot con base H2 en memoria)**:
   - Abre una terminal de PowerShell en el directorio raíz del proyecto.
   - Ejecuta:
     ```powershell
     .\run-backend.ps1
     ```
   - *Nota: Si no tienes Maven instalado globalmente, el script descargará una versión portable de forma automática para compilar y levantar el servidor en el puerto `8080`.*

2. **Iniciar el Microservicio de Analítica (FastAPI)**:
   - Abre otra terminal de PowerShell en el directorio raíz.
   - Ejecuta:
     ```powershell
     .\run-analytics.ps1
     ```
   - *Nota: El script creará un entorno virtual (`venv`), actualizará pip, instalará las dependencias de `requirements.txt` e iniciará el servidor en `http://localhost:8000`.*

3. **Acceder a la Aplicación**:
   - Abre tu navegador en: [http://localhost:8080/](http://localhost:8080/)
   - Inicia sesión con las credenciales de prueba pre-cargadas en el seeder automático:
     - 🎓 **Estudiante**: `estudiante@campus.edu` (Clave: `123456`)
     - 🍳 **Cocinero**: `cocinero@campus.edu` (Clave: `123456`)

---

### Opción B: Ejecución en Contenedores (Docker / MySQL)

Para levantar el ecosistema completo (MySQL + Backend + Analítica) simulando un entorno de producción real:

1. Asegúrate de tener Docker y Docker Compose instalados y activos.
2. Abre tu terminal en la raíz del proyecto.
3. Ejecuta:
   ```bash
   docker-compose up --build
   ```
4. Docker Compose levantará:
   - **`db`**: Servidor MySQL cargando automáticamente el script `database/init.sql` (crea tablas y siembra datos históricos para ver métricas de analítica de inmediato).
   - **`backend-spring`**: API expuesta en el puerto `8080`, conectada a MySQL en el perfil `prod`.
   - **`analytics-python`**: Microservicio expuesto en el puerto `8000`.

---

## 🚀 Despliegue en la Nube (Cloud Express)

Para cumplir con el **plus de la nota** y realizar el despliegue de forma rápida y gratuita:

1. **Base de Datos**:
   - Crea un proyecto en [Aiven.io](https://aiven.io/) o [Supabase](https://supabase.com/).
   - Inicializa una base de datos MySQL (Aiven) o PostgreSQL (Supabase) gratuita.
   - Copia la cadena de conexión provista.

2. **Backend & Frontend (Spring Boot)**:
   - Sube este repositorio a GitHub.
   - Crea un nuevo servicio web en [Render](https://render.com/) o [Railway](https://railway.app/).
   - Enlaza el repositorio y configura los siguientes parámetros en las variables de entorno de Render/Railway:
     - `SPRING_PROFILES_ACTIVE`: `prod`
     - `DB_HOST`: Host de tu base de datos cloud (ej. `mysql-aiven.com`)
     - `DB_PORT`: Puerto (ej. `3306`)
     - `DB_NAME`: Nombre de tu DB
     - `DB_USER`: Usuario
     - `DB_PASSWORD`: Contraseña
     - `ANALYTICS_SERVICE_URL`: URL pública donde se despliegue tu microservicio de Python.
   - Render compilará tu aplicación con Maven automáticamente y la servirá en una URL pública.

3. **Microservicio de Analítica (Python)**:
   - Crea otro servicio web en Render o Railway.
   - Enlaza la carpeta `analytics-python/` de tu repositorio.
   - Agrega la variable de entorno:
     - `BACKEND_URL`: URL pública de tu backend de Spring Boot (obtenida en el paso anterior).
   - Listo. ¡Tu microservicio procesará los datos desde Spring Boot en producción!
