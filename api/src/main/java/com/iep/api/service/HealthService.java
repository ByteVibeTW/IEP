package com.iep.api.service;


import org.springframework.stereotype.Service;

@Service
public class HealthService {
    /**
     * @return Health check response
     */
    public String healthCheck() {
        return "I am healthy";
    }
}