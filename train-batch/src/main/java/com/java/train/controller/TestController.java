package com.java.train.controller;

import com.java.train.feign.BussinessFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private BussinessFeign bussinessFeign;

    @GetMapping("/hello")
    public String hello() {
        String str = bussinessFeign.hello();
        LOG.info(str);
        return "Hello World! Batch";
    }
}