package com.liutyk.first_demo.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    @Value("${spring.application.version}")
    private String version;

    @GetMapping("/api/v1/")
    public Map<String, String> getStatus(){
        Map<String, String> map= new HashMap<>();
        map.put("version", version);
        return map;
    }
}
