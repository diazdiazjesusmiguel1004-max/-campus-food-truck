from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import os
import requests
from datetime import datetime
from collections import Counter, defaultdict

app = FastAPI(
    title="Campus Food Truck Analytics Service",
    description="Microservicio de Analitica de Ventas y Monitoreo en Tiempo Real",
    version="1.0.0"
)

# Permitir CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# URL del backend de Spring Boot (se puede configurar mediante variables de entorno)
BACKEND_URL = os.getenv("BACKEND_URL", "http://localhost:8080")

@app.get("/")
def read_root():
    return {
        "servicio": "Campus Food Truck Analytics",
        "estado": "Activo",
        "documentacion": "/docs"
    }

@app.get("/analytics/report")
def get_analytics_report():
    try:
        # 1. Consumir los datos del backend usando peticiones HTTP directas
        url = f"{BACKEND_URL}/api/pedidos/raw-data"
        response = requests.get(url, timeout=5)
        
        if response.status_code != 200:
            raise HTTPException(
                status_code=500,
                detail=f"Error al consumir datos de Spring Boot (Status {response.status_code})"
            )
            
        data = response.json()
        
        if not data:
            return {
                "platos_mas_vendidos": [],
                "horas_pico": {},
                "total_ventas": 0.0,
                "total_pedidos": 0,
                "mensaje": "No hay datos de ventas disponibles todavia."
            }
            
        # 2. Procesar las analiticas de forma pura y eficiente en Python
        # Estructura del item en data:
        # { "pedido_id", "producto_id", "nombre_producto", "cantidad", "precio_unitario", "total_pedido", "fecha_creacion", ... }
        
        plato_counter = Counter()
        horas_counter = Counter()
        pedidos_unicos = set()
        total_ventas = 0.0
        
        # Agrupaciones
        for item in data:
            producto = item.get("nombre_producto")
            cantidad = item.get("cantidad", 0)
            fecha_str = item.get("fecha_creacion")
            pedido_id = item.get("pedido_id")
            
            # Contar platos mas vendidos
            if producto:
                plato_counter[producto] += cantidad
                
            # Identificar pedidos unicos y sumar totales
            if pedido_id not in pedidos_unicos:
                pedidos_unicos.add(pedido_id)
                total_ventas += item.get("total_pedido", 0.0)
                
            # Contar horas pico (agrupado por hora: "08:00", "12:00", etc.)
            if fecha_str:
                try:
                    # Formato esperado: ISO-8601, ej. 2026-06-22T12:30:00
                    dt = datetime.fromisoformat(fecha_str.replace("Z", ""))
                    hora_clave = f"{dt.hour:02d}:00"
                    horas_counter[hora_clave] += 1
                except Exception:
                    pass

        # 3. Formatear la respuesta de la analitica
        platos_ordenados = [
            {"producto": k, "cantidad": v} for k, v in plato_counter.most_common()
        ]
        
        # Ordenar horas de forma cronologica
        horas_ordenadas = dict(sorted(horas_counter.items()))
        
        return {
            "platos_mas_vendidos": platos_ordenados,
            "horas_pico": horas_ordenadas,
            "total_ventas": round(total_ventas, 2),
            "total_pedidos": len(pedidos_unicos),
            "fecha_reporte": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        }
        
    except requests.exceptions.RequestException as e:
        raise HTTPException(
            status_code=503,
            detail=f"El backend de Spring Boot en {BACKEND_URL} no esta disponible o no responde. Detalles: {str(e)}"
        )
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Error interno procesando las analiticas: {str(e)}"
        )
