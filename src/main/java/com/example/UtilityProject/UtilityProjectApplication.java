package com.example.UtilityProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UtilityProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtilityProjectApplication.class, args);
	}

}
