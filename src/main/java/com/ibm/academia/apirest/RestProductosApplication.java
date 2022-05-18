package com.ibm.academia.apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@EnableEurekaClient
@SpringBootApplication
public class RestProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestProductosApplication.class, args);
	}

}
