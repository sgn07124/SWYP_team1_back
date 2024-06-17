package com.example.swyp_team1_back;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/cicdTest")
    public String home() {
        return "Hello CICD with Docker";
    }
}
