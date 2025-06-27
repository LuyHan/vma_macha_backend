package com.awsserver.awsserverapi;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HelloCtr {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot API!";
    }
    
    
}
