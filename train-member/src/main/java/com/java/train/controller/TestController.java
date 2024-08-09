package com.java.train.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// Nacos配置实时刷新
@RefreshScope
public class TestController {

    @Value("${test.nacos}")
    private String testNacos;

    @GetMapping("/hello")
    public String hello() {
        return "Hello " + testNacos;
    }
}