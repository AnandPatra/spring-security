package com.heraizen.security.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello/")
public class HelloController {

    @GetMapping("welcome")
    private ResponseEntity<String> welcomeMessage() {
        return ResponseEntity.ok().body("Welcome to Security app");
    }
}
