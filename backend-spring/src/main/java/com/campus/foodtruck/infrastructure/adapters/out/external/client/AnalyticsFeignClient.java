package com.campus.foodtruck.infrastructure.adapters.out.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "analytics-service", url = "${analytics.service.url:http://localhost:8000}")
public interface AnalyticsFeignClient {

    @GetMapping("/analytics/report")
    Map<String, Object> getSalesReport();
}
