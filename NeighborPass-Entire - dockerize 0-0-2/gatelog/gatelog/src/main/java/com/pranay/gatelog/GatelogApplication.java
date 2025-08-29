package com.pranay.gatelog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GatelogApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatelogApplication.class, args);
	}
	/// Challenges:
	/// On Delete set Null - Hibernate annotation
	/// Creating Entry, if visitor exists don't let the user enter the contact & name again
	}
