package com.hei.wallet.heiwallet.endpoint.rest.controller;

import com.hei.wallet.heiwallet.model.Dummy;
import com.hei.wallet.heiwallet.service.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HealthController {
    private final HealthService healthService;

    @GetMapping("/dummy-table")
    public List<Dummy> getDummies(){
        return healthService.getAllDummies();
    }

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }
}
