package com.qcon.HystrixSpringExample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class HystrixSpringExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(HystrixSpringExampleApplication.class, args);
	}
}
