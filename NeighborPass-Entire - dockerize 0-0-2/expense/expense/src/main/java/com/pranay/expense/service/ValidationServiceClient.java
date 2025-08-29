package com.pranay.expense.service;

import com.pranay.expense.controller.UserController;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(
        name = "validation-service",
        url = "http://localhost:8081"  // to be called
)
public interface ValidationServiceClient {
    @PostMapping("/auth/admin/register")
    ResponseEntity<?> registerAdmin(@RequestBody AdminRegisterValidationDto adminRegisterValidationDto);

    @PostMapping("/auth/register")
    ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO);
}