package com.example.diplomayinjava.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:4200")
public class TestController {

    @GetMapping("/cors")
    public String testCors() {
        return "CORS is working!";
    }

    @GetMapping("/health")
    public String health() {
        return "Server is running!";
    }
}












