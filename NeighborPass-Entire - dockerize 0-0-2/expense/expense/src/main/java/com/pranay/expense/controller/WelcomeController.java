package com.pranay.expense.controller;



import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class WelcomeController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String sayHelloAdmin() {
        return "Welcome Everyone (ADMIN)";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String sayHelloUser() {
        return "Welcome Everyone (USER)";
    }
}
