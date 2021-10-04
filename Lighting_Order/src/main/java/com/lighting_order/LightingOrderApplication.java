package com.lighting_order;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"request_generator","broker","controller"})
public class LightingOrderApplication {
	
	public static void main(String[] args) {	
		SpringApplication.run(LightingOrderApplication.class, args);
	}

}
