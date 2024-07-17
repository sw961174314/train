package com.java.train.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "business",url = "http://127.0.0.1:8002/business")
public interface BussinessFeign {

    @GetMapping("/hello")
    public String hello();
}