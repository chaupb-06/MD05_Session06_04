package com.banking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "Account Service is running and connected successfully to core-db (PostgreSQL database)!";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
