package com.springboot.appbanco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ServicioTransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioTransactionServiceApplication.class, args);
	}

}
