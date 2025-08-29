package com.pranay.gatelog.service;

import com.pranay.gatelog.dto.ApprovalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(
        name = "validation-service",
        url = "${feign.validation.url}"  // ← Points to Producer Service
)
public interface ValidationServiceClient {

    @PutMapping("/permission")
    void permission(@RequestBody ApprovalDto approvalDto);
}
