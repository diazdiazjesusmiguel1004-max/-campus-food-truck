package com.campus.foodtruck.infrastructure.adapters.out.external;

import com.campus.foodtruck.domain.ports.out.AnalyticsClientPort;
import com.campus.foodtruck.infrastructure.adapters.out.external.client.AnalyticsFeignClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AnalyticsClientAdapter implements AnalyticsClientPort {

    private final AnalyticsFeignClient feignClient;

    public AnalyticsClientAdapter(AnalyticsFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @Override
    public Map<String, Object> obtenerReporteVentas() {
        return feignClient.getSalesReport();
    }
}
