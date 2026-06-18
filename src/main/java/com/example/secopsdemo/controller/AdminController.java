package com.example.secopsdemo.controller;

import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("env", System.getenv().toString()); // VULN: Leaks environment data.
        return response;
    }

    @PostMapping("/diagnostics")
    public Map<String, String> diagnostics(@RequestParam String host) throws Exception {
        // VULN: Command injection risk. This endpoint is intentionally unsafe for scanners.
        Process process = Runtime.getRuntime().exec("sh -c ping -c 1 " + host);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        Map<String, String> response = new HashMap<>();
        response.put("output", output.toString());
        return response;
    }
}
