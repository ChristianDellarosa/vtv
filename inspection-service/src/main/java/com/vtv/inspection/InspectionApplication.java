package com.vtv.inspection;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class InspectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(InspectionApplication.class, args);
	}

}
