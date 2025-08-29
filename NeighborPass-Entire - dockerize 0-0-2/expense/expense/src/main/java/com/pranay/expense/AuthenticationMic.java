package com.pranay.expense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AuthenticationMic {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationMic.class, args);
	}

}
