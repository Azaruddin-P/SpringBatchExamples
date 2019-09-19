package com.example;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
@SpringBootApplication
@EnableBatchProcessing
@ImportResource("jobConfig.xml")
public class SpringBatchMySqLtoFlatfileApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchMySqLtoFlatfileApplication.class, args);
	}

}

