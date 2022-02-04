package com.heraizen.security.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello/")
@Slf4j
public class HelloController {


    //TODO: not working have to fix it
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("welcome")
    private ResponseEntity<String> welcomeMessage() {
        log.info("Role: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return ResponseEntity.ok().body("Welcome to Security app");
    }
}
