package com.iep.api.controller.api;

import com.iep.api.service.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {
    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    /**
     * 健康檢查
     * @return Health check response
     */
    @GetMapping()
    public String health() {
        return healthService.healthCheck();
    }
}