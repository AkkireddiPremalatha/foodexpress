package com.foodexpress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.foodexpress.feign")
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableScheduling
public class DeliveryManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryManagementServiceApplication.class, args);
	}

}
