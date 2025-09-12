package com.example.fedex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FedexApplication {

	public static void main(String[] args) {
		SpringApplication.run(FedexApplication.class, args);
	}

}
