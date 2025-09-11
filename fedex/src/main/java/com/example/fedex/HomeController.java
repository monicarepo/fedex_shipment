package com.example.fedex;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, Fedex App is running";
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }
}
