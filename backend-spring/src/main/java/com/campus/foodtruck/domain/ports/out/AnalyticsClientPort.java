package com.campus.foodtruck.domain.ports.out;

import java.util.Map;

public interface AnalyticsClientPort {
    Map<String, Object> obtenerReporteVentas();
}
